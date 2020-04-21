package cz.osu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.osu.data.GroupReport;
import cz.osu.data.Room;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseConnection
{
    private final static DatabaseConnection instance = new DatabaseConnection();
    private final static String baseURL = "http://localhost:8080/";
    private final static MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;

    private DatabaseConnection()
    {
        client = new OkHttpClient();
    }

    private String getResponse(String where) throws IOException
    {
        Request req = new Request.Builder().url(baseURL + where).build();
        Response resp = client.newCall(req).execute();
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

    public static DatabaseConnection getInstance()
    {
        return instance;
    }

    public ArrayList<Room> getListOfRooms() throws IOException
    {
        String data = getResponse("groups");

        Gson gson = new Gson();
        ArrayList<Room> result = gson.fromJson(data, new TypeToken<ArrayList<Room>>(){}.getType());

        return result;
    }

    public Double getRoomTemperature(Room room) throws IOException
    {
        return getRoomTemperature(room.getId());
    }

    public Double getRoomTemperature(int id) throws IOException
    {
        String data = getResponse("groups/" + id + "/temp");

        Gson gson = new Gson();
        return gson.fromJson(data, Double.class);
    }

    public Double getRoomTemperatureForce(int id) throws IOException
    {
        String data = getResponse("groups/" + id + "/heaterForce\"");

        Gson gson = new Gson();
        return gson.fromJson(data, Double.class);
    }

    public Double getRoomPowerConsumption(Room room) throws IOException
    {
        return getRoomPowerConsumption(room.getId());
    }

    public Double getRoomPowerConsumption(int id) throws IOException
    {
        String data = getResponse("groups/" + id + "/power");

        Gson gson = new Gson();
        return gson.fromJson(data, Double.class);
    }

    public boolean getRoomHeaterState(Room room) throws IOException
    {
        return getRoomHeaterState(room.getId());
    }

    public boolean getRoomHeaterState(int id) throws IOException
    {
        String data = getResponse("groups/" + id + "/heater");

        Gson gson = new Gson();
        return gson.fromJson(data, Boolean.class);
    }

    public boolean setRoomHeaterState(int id, boolean state) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("state", Boolean.toString(state))
                .build();

        Request req = new Request.Builder()
                .url(baseURL + "groups/" + id + "/heater")
                .post(body)
                .build();

        Response resp = client.newCall(req).execute();
        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }

    public boolean setRoomHeaterStateForce(int id, boolean state) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("state", Boolean.toString(state))
                .build();

        Request req = new Request.Builder()
                .url(baseURL + "groups/" + id + "/heaterForce")
                .post(body)
                .build();

        Response resp = client.newCall(req).execute();
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

    public boolean postNewRoom(Room room) throws IOException
    {
        return postNewRoom(room.getName());
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
        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }

    public boolean removeRoom(Room room) throws IOException
    {
        return removeRoom(room.getId());
    }

    public boolean removeRoom(int id) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", Integer.toString(id))
                .build();

        Request req = new Request.Builder()
                .url(baseURL + "/groups/" + id + "/remove")
                .post(body)
                .build();

        Response resp = client.newCall(req).execute();
        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }
}
