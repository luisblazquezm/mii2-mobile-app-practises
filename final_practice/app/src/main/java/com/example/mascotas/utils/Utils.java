package com.example.mascotas.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class Utils {

    public static String getCurrenDate() {
        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return day + "/" + month + "/" + year;
    }

    public static JSONObject extractJSONFromURL(URL url) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // Get input as bytes
            InputStream stream = connection.getInputStream();

            // Reader of bytes for content
            reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));

            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            // Parse to json
            JSONObject resultJSON = new JSONObject(buffer.toString());

            return resultJSON;
        } catch (JSONException e) {
            Log.e("e", "ERROR in JSON FROM URL PARSER: " + e.toString());

        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e("e", "ERROR in JSON FROM URL PARSER: " + e.toString());
            }
        }

        return null;
    }
}
