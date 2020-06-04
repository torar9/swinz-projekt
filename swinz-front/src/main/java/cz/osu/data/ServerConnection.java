package cz.osu.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;

public class ServerConnection
{
    private final static ServerConnection instance = new ServerConnection();
    private final static String baseURL = "http://localhost:8080/";
    private OkHttpClient client;

    private ServerConnection()
    {
        client = new OkHttpClient();
    }

    public static ServerConnection getInstance()
    {
        return instance;
    }

    private String getResponse(String where) throws IOException
    {
        Request req = new Request.Builder().url(baseURL + where).build();
        Response resp = client.newCall(req).execute();

        if(!resp.isSuccessful())
            throw new IOException();

        String result = resp.body().string();
        resp.close();

        return result;
    }

    public boolean testConnection()
    {
        try
        {
            Request req = new Request.Builder().url(baseURL + "/status").build();
            Response resp = client.newCall(req).execute();
            boolean result = resp.isSuccessful();
            resp.close();

            return result;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public ArrayList<Room> getListOfRooms() throws IOException
    {
        String data = getResponse("groups");

        Gson gson = new Gson();
        ArrayList<Room> result = gson.fromJson(data, new TypeToken<ArrayList<Room>>(){}.getType());

        return result;
    }

    public boolean setRoomHeaterStateForced(int id, boolean state) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("state", Boolean.toString(state))
                .build();

        Request req = new Request.Builder()
                .url(baseURL + "groups/" + id + "/heaterForced")
                .post(body)
                .build();

        Response resp = client.newCall(req).execute();
        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

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

    public boolean postNewRoom(String name) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .build();

        Request req = new Request.Builder()
                .url(baseURL + "groups/add")
                .post(body)
                .build();

        Response resp = client.newCall(req).execute();
        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }

    public double getGlobalTemp() throws IOException
    {
        String data = getResponse("groups/globalTemp");

        Gson gson = new Gson();

        return gson.fromJson(data, Double.class);
    }

    public boolean setGlobalTemp(double temp) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("temp", Double.toString(temp))
                .build();

        Request req = new Request.Builder()
                .url(baseURL + "groups/globalTemp")
                .post(body)
                .build();

        Response resp = client.newCall(req).execute();
        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }

    public boolean getGlobalHeaterState() throws IOException
    {
        String data = getResponse("groups/globalHeater");

        Gson gson = new Gson();

        return gson.fromJson(data, Boolean.class);
    }

    public boolean setGlobalHeaterState(boolean state) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("state", Boolean.toString(state))
                .build();

        Request req = new Request.Builder()
                .url(baseURL + "groups/globalHeater")
                .post(body)
                .build();

        Response resp = client.newCall(req).execute();
        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }

    public boolean setRoomTargetTemp(Room room, double temp) throws IOException
    {
        return setRoomTargetTemp(room.getId(), temp);
    }

    public boolean setRoomTargetTemp(int id, double temp) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("temp", Double.toString(temp))
                .build();

        Request req = new Request.Builder()
                .url(baseURL + "groups/" + id + "/targetTemp")
                .post(body)
                .build();

        Response resp = client.newCall(req).execute();
        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }

    public int getHeaterStat() throws IOException
    {
        String data = getResponse("groups/stats/heater");

        Gson gson = new Gson();

        return gson.fromJson(data, Integer.class);
    }

    public double getAvgRoomLightStat(Room room) throws IOException
    {
        String data = getResponse("groups/" + room.getId() + "/stats/lightWeeks");

        Gson gson = new Gson();

        return gson.fromJson(data, Double.class);
    }

    public ArrayList<RoomStats> getListOfMonthStats() throws IOException
    {
        String data = getResponse("groups/stats");

        Gson gson = new Gson();
        ArrayList<RoomStats> result = gson.fromJson(data, new TypeToken<ArrayList<RoomStats>>(){}.getType());

        return result;
    }
}
