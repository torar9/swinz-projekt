package cz.osu.Controllers;

import cz.osu.Main;
import cz.osu.RoomOverviewCell;
import cz.osu.data.DatabaseConnection;
import cz.osu.data.GroupReport;
import cz.osu.data.Room;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
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
    @FXML
    private ImageView roomHeaterImg;

    private ObservableList<Room> roomObservableList;
    private Timeline timer;
    private Room room;
    private DatabaseConnection db;

    public RoomOverviewController()
    {
        roomObservableList = FXCollections.observableArrayList();
        db = DatabaseConnection.getInstance();
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

                update();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        update();

        if(!roomObservableList.isEmpty())
        {
            room = roomObservableList.get(0);
            this.tempSliderLabel.setText(Double.toString(room.getTargetTemperature()));
            tempSlider.setValue(room.getTargetTemperature());
        }
    }

    private void update()
    {
        if(db.testConnection())
        {
            roomObservableList.clear();
            try
            {
                ArrayList<Room> list = db.getListOfRooms();
                for (Room r : list)
                {
                    GroupReport report = db.getRoomReport(r);
                    r.setReport(report);
                    if(room != null && room.getId() == r.getId())
                        room = r;
                    roomObservableList.add(r);
                }

                if(room != null)
                {
                    roomListView.getSelectionModel().select(room);

                    if(room.getHeaterState())
                        heaterStatusLabel.setText("Topení je zapnuto");
                    else heaterStatusLabel.setText("Topení je vypnuto");

                    timeLabel.setText(db.getAvgRoomLightStat(room) + " min");

                    GroupReport gp = room.getReport();
                    this.consumptionLabel.setText(Double.toString(gp.getPowerConsumption()));
                    this.tempLabel.setText(Double.toString(gp.getTemp()));
                    this.roomNameLabel.setText(room.getName());

                    boolean state = room.isForceHeater();

                    if(state)
                    {
                        Image img = new Image("images/status_green.png");
                        this.roomHeaterImg.setImage(img);
                    }
                    else
                    {
                        Image img = new Image("images/status_red.png");
                        this.roomHeaterImg.setImage(img);
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleOnItemClick()
    {
        timer.pause();
        Object o = roomListView.getSelectionModel().getSelectedItem();

        if(o != null)
        {
            this.room = (Room) roomListView.getSelectionModel().getSelectedItem();
            this.tempSliderLabel.setText(Double.toString(room.getTargetTemperature()));
            tempSlider.setValue(room.getTargetTemperature());
            update();
        }
        timer.play();
    }

    @FXML
    private void handleTempSliderDragEvent()
    {
        double temp = Math.round((tempSlider.getValue() * 10.0) / 10.0);
        tempSliderLabel.setText(Double.toString(temp));
    }

    @FXML
    private void handleTempSliderReleaseEvent()
    {
        try
        {
            double temp = Math.round((tempSlider.getValue() * 10.0) / 10.0);
            DatabaseConnection.getInstance().setRoomTargetTemp(room, temp);
            tempSliderLabel.setText(Double.toString(temp));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewButtonClick()
    {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setResizable(false);
        dialog.setTitle("Nová místnost");

        dialog.setHeaderText("Vytvoření nové místnosti");
        dialog.setContentText("Název mísnosti");

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent())
        {
            String name = result.get();
            if(!name.isEmpty())
            {
                try
                {
                    db.postNewRoom(name);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleRoomTempForce()
    {
        if(room != null)
        {
            boolean state = room.isForceHeater();
            try
            {
                db.setRoomHeaterStateForced(room.getId(), !state);
                update();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleHomeButtonClick()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"));

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
/*
roomObservableList.clear();
            try
            {
                ArrayList<Room> list = db.getListOfRooms();
                for (Room r : list)
                {
                    GroupReport report = db.getRoomReport(r);
                    r.setReport(report);
                    if(room != null && room.getId() == r.getId())
                        room = r;
                    roomObservableList.add(r);
                }
 */