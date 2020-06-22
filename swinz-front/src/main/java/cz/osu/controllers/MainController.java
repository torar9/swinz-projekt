package cz.osu.controllers;

import cz.osu.Main;
import cz.osu.cells.RoomListViewCell;
import cz.osu.data.*;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    @FXML
    private ListView mainList;
    @FXML
    private Label tempLabel;
    @FXML
    private Slider tempSlider;
    @FXML
    private ImageView tempButton;
    @FXML
    private Label mainHeaterLabel;

    private ObservableList<Room> roomObservableList;
    private Timeline timer;
    private RoomManager roomManager;
    private HouseManager houseManager;
    private StatisticsManager statManager;

    public MainController()
    {
        roomObservableList = FXCollections.observableArrayList();
        roomManager = new RoomManager();
        houseManager = new HouseManager();
        statManager = new StatisticsManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        mainList.setItems(roomObservableList);
        mainList.setCellFactory(studentListView -> new RoomListViewCell());

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

        init();
        update();
    }

    private void init()
    {
        try
        {
            mainHeaterLabel.setText("Bylo nutno zapnout vytápění " + statManager.getHeaterStatistics() + " dní za poslední rok");
            tempLabel.setText(Double.toString(houseManager.getGlobalTemperatureThreshold()));
            tempSlider.setValue(houseManager.getGlobalTemperatureThreshold());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void update()
    {
        roomObservableList.clear();
        try
        {
            boolean globalHeaterState= houseManager.getGlobalHeaterState();
            if(globalHeaterState)
            {
                Image img = new Image("images/status_green.png");
                tempButton.setImage(img);
            }
            else
            {
                Image img = new Image("images/status_red.png");
                tempButton.setImage(img);
            }

            ArrayList<Room> list = roomManager.getListOfRooms();
            for (Room r : list)
            {
                GroupReport report = roomManager.getRoomReport(r);
                r.setReport(report);
                roomObservableList.add(r);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRoomButtonClick()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/roomOverview.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 550, 550);
            Stage stage = new Stage();
            stage.setTitle("Přehled místností");
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

    @FXML
    private void handleStatButtonClick()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/statisticsView.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            Stage stage = new Stage();
            stage.setTitle("Statistiky");
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

    @FXML
    private void handleTempSliderDragEvent()
    {
        double temp = Math.round((tempSlider.getValue() * 10.0) / 10.0);
        tempLabel.setText(Double.toString(temp));
    }

    @FXML
    private void handleTempSliderReleaseEvent()
    {
        try
        {
            double temp = Math.round((tempSlider.getValue() * 10.0) / 10.0);
            houseManager.setGlobaltemperatureThreshold(temp);
            tempLabel.setText(Double.toString(temp));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTempButtonClick()
    {
        timer.pause();
        try
        {
            boolean state = houseManager.getGlobalHeaterState();
            houseManager.setGlobalHeaterState(!state);

            if(state)
            {
                Image img = new Image("images/status_red.png");
                tempButton.setImage(img);
            }
            else
            {
                Image img = new Image("images/status_green.png");
                tempButton.setImage(img);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        timer.play();
    }
}
