package cz.osu.Controllers;


import cz.osu.Main;
import cz.osu.RoomListViewCell;
import cz.osu.DatabaseConnection;
import cz.osu.data.GroupReport;
import cz.osu.data.Room;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable
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
    private Button roomButton;
    @FXML
    private Button statButton;
    @FXML
    private Label mainHeaterLabel;

    private ObservableList<Room> roomObservableList;
    private Timeline timer;
    private DatabaseConnection db;
    private ScheduledService<Void> service;

    public Controller()
    {
        db = DatabaseConnection.getInstance();
        roomObservableList = FXCollections.observableArrayList();
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
        if(db.testConnection())
        {
            roomButton.setDisable(false);
            statButton.setDisable(false);
            tempSlider.setDisable(false);

            try
            {
                update();

                mainHeaterLabel.setText("Bylo nutno zapnout vytápění " + Integer.toString(db.getHeaterStat()) + " dní za poslední 2 týdny");

                boolean globalHeaterState= db.getGlobalHeaterState();
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

                tempLabel.setText(Double.toString(db.getGlobalTemp()));
                tempSlider.setValue(db.getGlobalTemp());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            roomButton.setDisable(true);
            statButton.setDisable(true);
            tempSlider.setDisable(true);
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
                    roomObservableList.add(r);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRoomButtonClick()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = new File("src/main/java/cz/osu/fxml/roomOverview.fxml").toURI().toURL();
            fxmlLoader.setLocation(url);

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
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = new File("src/main/java/cz/osu/fxml/statisticsView.fxml").toURI().toURL();
            fxmlLoader.setLocation(url);

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
    private void handleTempButtonClick()
    {
        if(db.testConnection())
        {
            try
            {
                boolean state = db.getGlobalHeaterState();
                db.setGlobalHeaterState(!state);

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
        }
    }

    @FXML
    private void handleTempSliderDragEvent()
    {
        if(db.testConnection())
        {
            double temp = Math.round((tempSlider.getValue() * 10.0) / 10.0);
            tempLabel.setText(Double.toString(temp));
        }
    }

    @FXML
    private void handleTempSliderReleaseEvent()
    {
        if(db.testConnection())
        {
            try
            {
                double temp = Math.round((tempSlider.getValue() * 10.0) / 10.0);
                DatabaseConnection.getInstance().setGlobalTemp(temp);
                tempLabel.setText(Double.toString(temp));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
