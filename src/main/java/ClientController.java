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

    @FXML
    public void setDirectory() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File iniFile = new File("settings.ini");
        Ini ini = new Ini(iniFile);
        File initialDirectory = new File(ini.get("Settings", "lastDirectoryUsed"));
        if (initialDirectory.exists()) {
            directoryChooser.setInitialDirectory(initialDirectory);
        }
        Stage stage = (Stage) anchorid.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        String importPath = selectedDirectory.getAbsolutePath();
        fileEdit.setDirectory(importPath + "\\");
        fileEdit.fileImport();
        directoryLabel.setText("Selected Directory: \n" +  importPath + "\\");
        filesLabel.setText(FileEdit.files.size() + " files added");
    }

    @FXML
    public void openSettings() throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Bitrate Converter");
        primaryStage.setScene(new Scene(newRoot));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    public void executeChanges() throws IOException, NotSupportedException, EncoderException, InvalidDataException, UnsupportedTagException {
        fileEdit.setBitrate();
        directoryLabel.setText("Selected Directory:");
        filesLabel.setText("0 files added");
    }

}