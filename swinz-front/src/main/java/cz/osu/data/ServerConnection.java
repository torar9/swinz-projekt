package cz.osu.data;

import okhttp3.*;

import java.io.IOException;

public class ServerConnection
{
    private final static String baseURL = "http://localhost:8080/";
    private OkHttpClient client;

    public ServerConnection()
    {
        client = new OkHttpClient();
    }

    protected String getResponse(String where) throws IOException
    {
        Request req = new Request.Builder().url(baseURL + where).build();
        Response resp = client.newCall(req).execute();

        if(!resp.isSuccessful())
            throw new IOException();

        String result = resp.body().string();
        resp.close();

        return result;
    }

    protected Response postRequest(String paramName, String param, String path) throws IOException
    {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(paramName, param)
                .build();

        Request req = new Request.Builder()
                .url(baseURL + path)
                .post(body)
                .build();

        return client.newCall(req).execute();
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
}
