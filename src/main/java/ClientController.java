import com.mpatric.mp3agic.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.ini4j.Ini;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;

public class ClientController {

    @FXML private AnchorPane anchorid;
    @FXML private Label directoryLabel;
    @FXML private Label filesLabel;

    FileEdit fileEdit = new FileEdit();

    // Sets the directory of the files
    @FXML
    public void setDirectory() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File iniFile = new File("settings.ini");
        Ini ini = new Ini(iniFile);
        // Gets the directory in 'settings.ini' with key 'lastDirectoryUsed'
        File initialDirectory = new File(ini.get("Settings", "lastDirectoryUsed"));
        // If the last directory exists, then set it to the initial directory
        if (initialDirectory.exists()) {
            directoryChooser.setInitialDirectory(initialDirectory);
        }
        // Open a directory chooser for the user to pick a directory
        Stage stage = (Stage) anchorid.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        String importPath = selectedDirectory.getAbsolutePath();
        // Sets the directory for Bitrate Conversion
        fileEdit.setDirectory(importPath + "\\");
        fileEdit.fileImport();
        directoryLabel.setText("Selected Directory: \n" +  importPath + "\\");
        filesLabel.setText(FileEdit.files.size() + " files added");
    }

    // Opens the settings window
    @FXML
    public void openSettings() throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Bitrate Converter");
        primaryStage.setScene(new Scene(newRoot));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Executes the updates for each file based on the settings chosen
    @FXML
    public void executeChanges() throws IOException, NotSupportedException, EncoderException, InvalidDataException, UnsupportedTagException {
        fileEdit.setBitrate();
        directoryLabel.setText("Selected Directory:");
        filesLabel.setText("0 files added");
    }

}