package cz.osu.controllers;

import cz.osu.Main;
import cz.osu.MonthStatCell;
import cz.osu.data.RoomStats;
import cz.osu.data.StatisticsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable
{
    @FXML
    private ListView mainStatList;
    private StatisticsManager statManager;
    private ObservableList<RoomStats> roomStatList;

    public StatisticsController()
    {
        roomStatList = FXCollections.observableArrayList();
        statManager = new StatisticsManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            List<RoomStats> list = statManager.getRoomStatisticsPerMonth();

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainView.fxml"));

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
