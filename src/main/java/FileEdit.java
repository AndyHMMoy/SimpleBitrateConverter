import com.mpatric.mp3agic.*;
import org.ini4j.Ini;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FileEdit {

    public static ArrayList<File> files = new ArrayList<>();

    public static String PATH_NAME;

    // Sets the directory value inside the 'settings.ini' for 'lastDirectoryUsed' key
    public void setDirectory(String path) throws IOException {
        PATH_NAME = path;
        Ini ini = new Ini(new File("settings.ini"));
        ini.put("Settings", "lastDirectoryUsed", path);
        ini.store();
    }

    // Gets all files inside the specified directory adds them to the 'files' arraylist
    public void fileImport() {
        files.clear();
        File file = new File(PATH_NAME);
        files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(file.listFiles())));
    }

    // Updates all files in the 'files' arraylist
    public void setBitrate() throws InvalidDataException, UnsupportedTagException, IOException, EncoderException, NotSupportedException {
        ArrayList<byte[]> albumArt = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File source = files.get(i);
            Mp3File mp3File = new Mp3File(source.getPath());
            ID3v2 tags = mp3File.getId3v2Tag();
            String sourcePath = source.getPath();
            sourcePath = sourcePath.replace(".mp3", "a.mp3");
            // Renames the file to a include an 'a' character at the end of the file name.
            // We treat this as a temporary file because the library cannot have files of the same name
            File target = new File(sourcePath);
            // Adds the album art to an arraylist as the encoding will remove it from the mp3 file
            albumArt.add(tags.getAlbumImage());
            File file = new File(sourcePath);
            Ini ini = new Ini(new File("settings.ini"));
            int bitrate = Integer.parseInt(ini.get("Settings", "bitrate"));
            // Only encode the file if the bitrate does not match, i.e. the bitrate set for conversion does not match with the file's bitrate
            if (bitrate != mp3File.getBitrate()) {
                EncodingAttributes attrs = prepareEncoder();
                Encoder encoder = new Encoder();
                encoder.encode(new MultimediaObject(source), target, attrs);
                source.delete();
                mp3File = new Mp3File(sourcePath);
                tags = mp3File.getId3v2Tag();
                // Sets the album art back onto the file after it is lost from encoding
                tags.setAlbumImage(albumArt.get(i), "image/png");
                mp3File.setId3v2Tag(tags);
            }
            // Sets the name of the file in the format 'artist - title' and saves and deletes the temporary file and the original file
            String saveName = tags.getArtist() + " - " + tags.getTitle() + ".mp3";
            saveName = saveName.replaceAll("[?<>:*]", "");
            System.out.println(PATH_NAME + saveName);
            mp3File.save(PATH_NAME + saveName);
            file.delete();
            source.delete();
        }
    }

    // Prepares all parameters for the encoding; All settings are obtained from the 'settings.ini' file
    private EncodingAttributes prepareEncoder() throws IOException {
        Ini ini = new Ini(new File("settings.ini"));
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec(ini.get("Settings", "codec"));
        audio.setBitRate(Integer.parseInt(ini.get("Settings", "bitrate")));
        audio.setChannels(Integer.parseInt(ini.get("Settings", "channels")));
        audio.setSamplingRate(Integer.parseInt(ini.get("Settings", "samplingRate")));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat(ini.get("Settings", "outputFormat"));
        attrs.setAudioAttributes(audio);
        return attrs;
    }

}
