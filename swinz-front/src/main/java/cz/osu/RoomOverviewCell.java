package cz.osu;

import cz.osu.data.Room;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

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
