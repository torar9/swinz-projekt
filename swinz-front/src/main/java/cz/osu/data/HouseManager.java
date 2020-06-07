package cz.osu.data;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class HouseManager extends ServerConnection
{
    public HouseManager()
    {
        super();
    }

    public double getGlobalTemperatureThreshold() throws IOException
    {
        String data = getResponse("groups/globalTemp");

        Gson gson = new Gson();

        return gson.fromJson(data, Double.class);
    }

    public boolean setGlobaltemperatureThreshold(double temp) throws IOException
    {
        Response resp = postRequest("temp", Double.toString(temp), "groups/globalTemp");

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
        Response resp = postRequest("state", Boolean.toString(state), "groups/globalHeater");

        if(!resp.isSuccessful())
            throw new IOException();

        boolean result = resp.isSuccessful();
        resp.close();

        return result;
    }
}
