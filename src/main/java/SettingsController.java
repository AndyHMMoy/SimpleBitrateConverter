import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class SettingsController {

    @FXML ComboBox bitrateSelection;
    @FXML ComboBox channelSelection;
    @FXML ComboBox samplingSelection;

    public SettingsController() {
    }

    public void initialize() throws IOException {
        Ini ini = new Ini(new File("settings.ini"));
        bitrateSelection.setValue(String.valueOf(Integer.parseInt(ini.get("Settings", "bitrate")) / 1000));
        channelSelection.setValue(ini.get("Settings", "channels"));
        samplingSelection.setValue(ini.get("Settings", "samplingRate"));
    }

    @FXML
    public void saveSettings() throws IOException {
        Ini ini = new Ini(new File("settings.ini"));
        ini.put("Settings", "bitrate", Integer.parseInt(bitrateSelection.getValue().toString()) * 1000);
        ini.put("Settings", "channels", channelSelection.getValue());
        ini.put("Settings", "samplingRate", samplingSelection.getValue());
        ini.store();
    }

    @FXML
    public void resetSettings() throws IOException {
        bitrateSelection.setValue("192");
        channelSelection.setValue("2");
        samplingSelection.setValue("44100");
        Ini ini = new Ini(new File("settings.ini"));
        ini.put("Settings", "bitrate", "192000");
        ini.put("Settings", "channels", "2");
        ini.put("Settings", "samplingRate", "44100");
        ini.store();
    }

}
