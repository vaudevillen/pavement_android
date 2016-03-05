package com.pnpc.pavement;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by jjshin on 3/4/16.
 */
public class ReadingTask extends AsyncTask<JSONObject, Void, String> {


    @Override
    protected String doInBackground(JSONObject... params) {
        Log.i("ReadingTask", "reading task called");
        URL url;
        HttpURLConnection connection;
        String jsonResponse = null;
        JSONObject jsonObj = params[0];
        try {
            url = new URL("http://192.168.2.196:3000/reading");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Writer writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(jsonObj.toString());
// json data
            writer.close();
            InputStream inputStream = connection.getInputStream();
//input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            jsonResponse = buffer.toString();
//response data

            //send to post execute
            Log.i("Post Response", "" + jsonResponse);
            return jsonResponse;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
}