package cz.osu.Controllers;

import cz.osu.DatabaseConnection;
import cz.osu.MonthStatCell;
import cz.osu.RoomListViewCell;
import cz.osu.data.Room;
import cz.osu.data.RoomStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

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
}
