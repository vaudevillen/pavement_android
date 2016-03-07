package com.pnpc.pavement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


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
            url = new URL("https://10.0.2.2:3000/readings");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.writeBytes(jsonObj.toString());
// json data
            writer.flush();
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