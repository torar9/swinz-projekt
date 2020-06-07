package cz.osu.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;

public class RoomManager extends ServerConnection
{
    public RoomManager()
    {
        super();
    }

    public ArrayList<Room> getListOfRooms() throws IOException
    {
        String data = getResponse("groups");

        Gson gson = new Gson();
        ArrayList<Room> result = gson.fromJson(data, new TypeToken<ArrayList<Room>>(){}.getType());

        return result;
    }

    public GroupReport getRoomReport(Room room) throws IOException
    {
        return getRoomReport(room.getId());
    }

    public GroupReport getRoomReport(int id) throws IOException
    {
        String data = getResponse("groups/" + id + "/report");

        Gson gson = new Gson();

        return gson.fromJson(data, GroupReport.class);
    }

    public boolean createNewRoom(String name) throws IOException
    {
        Response resp = postRequest("name", name, "groups/add");
        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }

    public boolean setRoomThresholdTemperature(Room room, double temp) throws IOException
    {
        return setRoomThresholdTemperature(room.getId(), temp);
    }

    public boolean setRoomThresholdTemperature(int id, double temp) throws IOException
    {
        Response resp = postRequest("temp", Double.toString(temp), "groups/" + id + "/targetTemp");

        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }

    public boolean setGlobalRoomHeaterState(int id, boolean state) throws IOException
    {
        Response resp = postRequest("state", Boolean.toString(state), "groups/" + id + "/heaterForced");

        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }
}
