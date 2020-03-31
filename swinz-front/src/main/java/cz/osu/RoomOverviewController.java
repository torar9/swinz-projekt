package cz.osu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RoomOverviewController implements Initializable
{
    @FXML
    private Slider tempSlider;
    @FXML
    private Label tempSliderLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label heaterStatusLabel;
    @FXML
    private Label roomNameLabel;
    @FXML
    private Label consumptionLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ListView roomListView;

    private ObservableList<Room> roomObservableList;
    private Timeline timer;

    public RoomOverviewController()
    {
        roomObservableList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        roomListView.setItems(roomObservableList);
        roomListView.setCellFactory(studentListView -> new RoomOverviewCell());

        timer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        roomListView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Room room = (Room) roomListView.getSelectionModel().getSelectedItem();
                System.out.println("Room id: " + room.getId());
                System.out.println("Room name: " + room.getName());
            }
        });

        update();
    }

    private void update()
    {
        roomObservableList.clear();
        try
        {
            DatabaseConnection db = DatabaseConnection.getInstance();
            ArrayList<Room> list = db.getListOfRooms();
            for (Room r : list)
            {
                GroupReport report = db.getRoomReport(r);
                r.setReport(report);
                roomObservableList.add(r);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void handleOnItemClick()
    {
    }

    @FXML
    private void handleNewButtonClick()
    {
    }

    @FXML
    private void handleHomeButtonClick()
    {
    }
}
