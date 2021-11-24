import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

   public static void main(String[] args) throws IOException {
       launch(args);
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Client.fxml"));
       Scene scene = new Scene(root);
       primaryStage.setTitle("Bitrate Converter");
       primaryStage.setScene(scene);
       primaryStage.setResizable(false);
       primaryStage.show();
   }

}
