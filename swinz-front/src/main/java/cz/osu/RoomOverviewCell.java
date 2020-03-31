package cz.osu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RoomOverviewCell extends ListCell<Room>
{
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
            setText(room.getName());
        }
    }
}
