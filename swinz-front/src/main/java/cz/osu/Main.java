package cz.osu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("SWINZ");
        Scene scene = new Scene(root, 500, 500);
        //scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        Main.stage = primaryStage;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
