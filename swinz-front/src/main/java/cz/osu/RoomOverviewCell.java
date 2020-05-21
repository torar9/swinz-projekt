package cz.osu;

import cz.osu.data.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RoomOverviewCell extends ListCell<Room>
{
    @FXML
    private Label roomName;
    @FXML
    GridPane mainPane;
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
                    URL url = new File("src/main/java/cz/osu/fxml/roomOverviewCell.fxml").toURI().toURL();

                    mLLoader = new FXMLLoader(getClass().getResource("/fxml/roomOverviewCell.fxml"));
                    mLLoader.setController(this);

                    mLLoader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            roomName.setText(room.getName());

            setText(null);
            setGraphic(mainPane);
        }
    }
}
