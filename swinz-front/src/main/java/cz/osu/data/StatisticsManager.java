package cz.osu.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

public class StatisticsManager extends ServerConnection
{
    public StatisticsManager()
    {
        super();
    }

    public int getHeaterStatistics() throws IOException
    {
        String data = getResponse("groups/stats/heater");

        Gson gson = new Gson();

        return gson.fromJson(data, Integer.class);
    }

    public double getAverageRoomLightOnTwoWeeksStatistic(Room room) throws IOException
    {
        String data = getResponse("groups/" + room.getId() + "/stats/lightWeeks");

        Gson gson = new Gson();

        return gson.fromJson(data, Double.class);
    }

    public ArrayList<RoomStats> getRoomStatisticsPerMonth() throws IOException
    {
        String data = getResponse("groups/stats");

        Gson gson = new Gson();
        ArrayList<RoomStats> result = gson.fromJson(data, new TypeToken<ArrayList<RoomStats>>(){}.getType());

        return result;
    }
}
