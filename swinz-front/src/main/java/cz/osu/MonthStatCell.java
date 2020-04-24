package cz.osu;

import cz.osu.data.RoomMonthStatistics;
import cz.osu.data.RoomStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MonthStatCell extends ListCell<RoomStats> implements Initializable
{
    public class RoomMonthStatCell extends ListCell<RoomMonthStatistics>
    {
        @FXML
        private Label roomLabel;
        @FXML
        private Label avgHeaterLabel;
        @FXML
        private Label avgLightLabel;
        @FXML
        private Label consumptionLabel;
        @FXML
        private AnchorPane viewPaneMonth;

        private FXMLLoader mLLoaderSub;

        @Override
        protected void updateItem(RoomMonthStatistics stat, boolean empty)
        {
            super.updateItem(stat, empty);

            if(empty || stat == null)
            {
                this.setText(null);
                this.setGraphic(null);
            }
            else
            {
                if (mLLoaderSub == null)
                {
                    try
                    {
                        URL url = new File("src/main/java/cz/osu/fxml/statCell.fxml").toURI().toURL();

                        mLLoaderSub = new FXMLLoader(url);
                        mLLoaderSub.setController(this);

                        mLLoaderSub.load();
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

                this.setText(null);
                this.setGraphic(viewPaneMonth);
            }
        }
    }

    @FXML
    private Label monthLabel;
    @FXML
    private ListView monthStatList;
    @FXML
    private AnchorPane viewPane;

    private FXMLLoader mLLoader;
    private ObservableList<RoomMonthStatistics> roomMonthlyStatsList;

    public MonthStatCell()
    {
        roomMonthlyStatsList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        monthStatList.setItems(roomMonthlyStatsList);
        monthStatList.setCellFactory(studentListView -> new RoomMonthStatCell());
    }

    @Override
    protected void updateItem(RoomStats stats, boolean empty)
    {
        super.updateItem(stats, empty);

        if(empty || stats == null)
        {
            this.setText(null);
            this.setGraphic(null);
        }
        else
        {
            if (mLLoader == null)
            {
                try
                {
                    URL url = new File("src/main/java/cz/osu/fxml/monthStatCell.fxml").toURI().toURL();

                    mLLoader = new FXMLLoader(url);
                    mLLoader.setController(this);

                    mLLoader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            monthLabel.setText(stats.getMonthName());

            this.setText(null);
            this.setGraphic(viewPane);
        }
    }
}
