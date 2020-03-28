package cz.osu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RoomOverviewCell extends ListCell<Room>
{
    @FXML
    private Label roomName;

    private int roomID;
    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Room room, boolean empty)
    {
        super.updateItem(room, empty);
        super.setText(room.getName());
    }

    public int getRoomID()
    {
        return roomID;
    }
}
