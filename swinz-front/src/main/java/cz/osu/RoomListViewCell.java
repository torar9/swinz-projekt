package cz.osu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RoomListViewCell extends ListCell<Room>
{
    @FXML
    ImageView imageStatus;
    @FXML
    Label mainRoomLabel;
    @FXML
    Label mainTempLabel;
    @FXML
    GridPane mainListGrid;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Room room, boolean empty)
    {
        super.updateItem(room, empty);

        if(empty || room == null)
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
                    URL url = new File("src/main/java/cz/osu/mainListCell.fxml").toURI().toURL();

                    mLLoader = new FXMLLoader(url);
                    mLLoader.setController(this);

                    mLLoader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            mainRoomLabel.setText(room.getName());
            if(room.getReport() != null)
            {
                mainTempLabel.setText(room.getReport().getTemp() + " °C");
            }
            mainTempLabel.setText(room.getReport().getTemp() + " °C");

            if(room.getHeaterState())
            {
                Image img = new Image("images/status_green.png");
                imageStatus.setImage(img);
            }
            else
            {
                Image img = new Image("images/status_red.png");
                imageStatus.setImage(img);
            }

            setText(null);
            setGraphic(mainListGrid);
        }

    }

}
