package com.example.datospersonalistalmacen.utils;

import android.os.Environment;
import android.util.Xml;

import com.example.datospersonalistalmacen.UnaPersona;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class Utils {
    // This dependency was added in /app/build.gradle file
    // In the 'dependencies' part including the following line and rebuilding project:
    // implementation 'com.google.code.gson:gson:2.8.6'
    private static Gson jsonHandler = new Gson();

    // XML Serializers
    private static XmlSerializer xmlHandler = Xml.newSerializer();
    private static StringWriter writer = new StringWriter();

    public static String toJSON(Object o) {
        return Utils.jsonHandler.toJson(o);
    }

    public static String toXML(Object o) {

        // Convert to JSON string
        String jsonContent = toJSON(o);

        try {
            // Convert to JSON object
            JSONObject json = new JSONObject(jsonContent);

            // Format XML content
            Utils.xmlHandler.setOutput(Utils.writer);
            Utils.xmlHandler.startDocument("UTF-8", true);
            Utils.xmlHandler.startTag("", "users");

            // Iterate over content
            Iterator<String> keys = json.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                Utils.xmlHandler.startTag("", key);
                Utils.xmlHandler.text(String.valueOf(json.get(key)));
                Utils.xmlHandler.endTag("", key);
            }

            Utils.xmlHandler.endTag("", "users");
            Utils.xmlHandler.endDocument();

            // Return result in XML format
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String multipleItemsToXML(Object o) {
        String xmlResult = "";

        // Convert to JSON string
        String jsonContent = toJSON(o);

        try {
            // Convert to JSON object
            JSONArray json = new JSONArray(jsonContent);

            for (int i = 0; i < json.length(); i++) {
                xmlResult += Utils.toXML(json.get(i));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return xmlResult;
    }

    public static int exportToFile(String filename, byte[] data) {
        /*
         * TO WRITE IN EXTERNAL MEMORY ITS NECESSARY THE PERMISSION IN THE MANIFEST FILE including line:
         * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
         */

        // Check if filename is json type
        if (!filename.toLowerCase().endsWith(".json"))
            return Constants.FILE_CODES.INCORRECT_FILENAME.ordinal();

        // Export to file in External Memory
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        FileOutputStream fos;

        // Transform to bytes for file outputstream
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            return Constants.FILE_CODES.FAILURE.ordinal();
        } catch (IOException e) {
            return Constants.FILE_CODES.IO_FAILURE.ordinal();
        }

        return Constants.FILE_CODES.SUCCESS.ordinal();
    }
}
