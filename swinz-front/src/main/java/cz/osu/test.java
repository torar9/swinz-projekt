package cz.osu;

import cz.osu.data.Room;
import cz.osu.data.RoomMonthStatistics;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class test extends ListCell<RoomMonthStatistics>
{
    @FXML
    private Label roomLabel;
    @FXML
    private Label avgHeaterLabel;
    @FXML
    private Label avgLightLabel;
    @FXML
    private Label consumptionLabel;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(RoomMonthStatistics stat, boolean empty)
    {
        super.updateItem(stat, empty);

        if(empty || stat == null)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {
            if (mLLoader == null)
            {
                try
                {
                    URL url = new File("src/main/java/cz/osu/fxml/statCell.fxml").toURI().toURL();

                    mLLoader = new FXMLLoader(url);
                    mLLoader.setController(this);

                    mLLoader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            roomLabel.setText(stat.getRoomName());
            avgHeaterLabel.setText(stat.getDaysHeaterOn().toString());
            avgLightLabel.setText(stat.getAverageDayLight().toString());
            consumptionLabel.setText(stat.getPowerConsumption().toString());
        }
    }
}
