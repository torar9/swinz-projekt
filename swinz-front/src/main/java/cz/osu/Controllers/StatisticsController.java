package cz.osu.Controllers;

import cz.osu.DatabaseConnection;
import cz.osu.Main;
import cz.osu.MonthStatCell;
import cz.osu.data.RoomStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable
{
    @FXML
    private ListView mainStatList;
    private DatabaseConnection db;
    private ObservableList<RoomStats> roomStatList;

    public StatisticsController()
    {
        db = DatabaseConnection.getInstance();
        roomStatList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            List<RoomStats> list = db.getListOfMonthStats();

            mainStatList.setItems(roomStatList);
            mainStatList.setCellFactory(studentListView -> new MonthStatCell());

            for (RoomStats stats : list)
            {
                roomStatList.add(stats);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHomeButtonClick()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = new File("src/main/java/cz/osu/fxml/sample.fxml").toURI().toURL();
            fxmlLoader.setLocation(url);

            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            Stage stage = new Stage();
            stage.setTitle("Swinz");
            stage.setScene(scene);
            stage.show();

            Main.stage.close();
            Main.stage = stage;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
