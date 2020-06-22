package cz.osu.cells;

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
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MonthStatCell extends ListCell<RoomStats> implements Initializable
{
    public class RoomMonthStatCell extends ListCell<RoomMonthStatistics> implements Initializable
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
        public void initialize(URL location, ResourceBundle resources)
        {
        }

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
                        mLLoaderSub = new FXMLLoader(getClass().getResource("/fxml/statCell.fxml"));
                        mLLoaderSub.setController(this);

                        mLLoaderSub.load();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                this.roomLabel.setText(stat.getRoomName());
                this.avgHeaterLabel.setText(stat.getDaysHeaterOn().toString());
                this.avgLightLabel.setText(stat.getAverageDayLight().toString() + " min");
                this.consumptionLabel.setText(stat.getPowerConsumption().toString() + " W");

                this.setText(null);
                this.setGraphic(viewPaneMonth);
            }
        }
    }

    @FXML
    private Label monthLabel;
    @FXML
    private TextArea consmpLabel;
    @FXML
    private TextArea lightLabel;
    @FXML
    private TextArea heaterLabel;
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
        consmpLabel.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
        heaterLabel.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
        lightLabel.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
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
                    mLLoader = new FXMLLoader(getClass().getResource("/fxml/monthStatCell.fxml"));
                    mLLoader.setController(this);

                    mLLoader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            monthLabel.setText(stats.getMonthName());

            roomMonthlyStatsList.clear();
            List<RoomMonthStatistics> list = stats.getList();
            for(RoomMonthStatistics stat : list)
            {
                this.roomMonthlyStatsList.add(stat);
            }

            this.setText(null);
            this.setGraphic(viewPane);
        }
    }

    public ObservableList<RoomMonthStatistics> getRoomMonthlyStatsList()
    {
        return roomMonthlyStatsList;
    }
}
