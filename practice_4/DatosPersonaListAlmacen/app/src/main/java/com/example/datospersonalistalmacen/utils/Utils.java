package com.example.datospersonalistalmacen.utils;

import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.example.datospersonalistalmacen.UnaPersona;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

    public static String toXML(JSONArray json) {

        try {
            // Format XML content
            Utils.xmlHandler.setOutput(Utils.writer);
            Utils.xmlHandler.startDocument("UTF-8", true);
            Utils.xmlHandler.startTag("", "users");

            // Iterate over content
            for (int i = 0; i < json.length(); i++) {
                Utils.xmlHandler.startTag("", "item");
                JSONObject j = json.getJSONObject(i);
                Iterator<String> keys =  j.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    Utils.xmlHandler.startTag("", key);
                    Utils.xmlHandler.text(String.valueOf(j.get(key)));
                    Utils.xmlHandler.endTag("", key);
                }
                Utils.xmlHandler.endTag("", "item");
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

            // Convert JSON array to XML
            xmlResult = Utils.toXML(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return xmlResult;
    }

    public static int exportToFile(ContextWrapper cw, String filename, byte[] data) {
        /*
         * TO WRITE IN EXTERNAL MEMORY ITS NECESSARY THE PERMISSION IN THE MANIFEST FILE including line:
         * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
         */

        // Check if filename is json type
        if (!filename.toLowerCase().endsWith(".json") && !filename.toLowerCase().endsWith(".xml"))
            return Constants.FILE_CODES.INCORRECT_FILENAME.ordinal();

        File directory = cw.getExternalFilesDir(Environment.getExternalStorageDirectory().getAbsolutePath());

        // Export to file in External Memory
        File file = new File(directory, filename);
        FileOutputStream fos;

        Log.d("d", "Filepath: " + file.toString());

        // Transform to bytes for file outputstream
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("e", "File not found error: " + e.toString());
            return Constants.FILE_CODES.FAILURE.ordinal();
        } catch (IOException e) {
            Log.e("e", "IO error: " + e.toString());
            return Constants.FILE_CODES.IO_FAILURE.ordinal();
        }

        return Constants.FILE_CODES.SUCCESS.ordinal();
    }

    public static JSONObject extractXMLFromURL(URL url) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        JSONArray jsonUsersArray = new JSONArray();

        try {
            // Build XML document tree
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));

            // Normalize tree
            doc.getDocumentElement().normalize();

            // Get node list
            NodeList nodeList = doc.getElementsByTagName(Constants.PERSONA_URL_KEY);

            // Iterate over XML and get attributes and values
            for (int i = 0; i < nodeList.getLength(); i++) {
                JSONObject j = new JSONObject();

                // Get node and parse to element
                Node node = nodeList.item(i);
                Element element = (Element) node;

                // Get name
                NodeList nameList = element.getElementsByTagName(Constants.XML_URL_NAME_PARAMETER);
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                j.put(Constants.JSON_URL_NAME_PARAMETER, ((Node) nameList.item(0)).getNodeValue());

                // Get surename
                nameList = element.getElementsByTagName(Constants.XML_URL_SURENAMES_PARAMETER);
                nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                j.put(Constants.JSON_URL_SURENAMES_PARAMETER, ((Node) nameList.item(0)).getNodeValue());

                // Get phone
                nameList = element.getElementsByTagName(Constants.XML_URL_PHONE_PARAMETER);
                nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                j.put(Constants.JSON_URL_PHONE_PARAMETER, ((Node) nameList.item(0)).getNodeValue());

                // Get driving license
                nameList = element.getElementsByTagName(Constants.XML_URL_DRIVING_LICENSE_PARAMETER);
                nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                j.put(Constants.JSON_URL_DRIVING_LICENSE_PARAMETER, ((Node) nameList.item(0)).getNodeValue());

                // Get english level
                nameList = element.getElementsByTagName(Constants.XML_URL_ENGLISH_LEVEL_PARAMETER);
                nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                j.put(Constants.JSON_URL_ENGLISH_LEVEL_PARAMETER, ((Node) nameList.item(0)).getNodeValue());

                // Get registry date
                nameList = element.getElementsByTagName(Constants.XML_URL_REGISTRY_DATE_PARAMETER);
                nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                j.put(Constants.JSON_URL_REGISTRY_DATE_PARAMETER, ((Node) nameList.item(0)).getNodeValue());

                // Add to json array
                jsonUsersArray.put(j);
            }

            // Return agenda
            JSONObject agenda = new JSONObject();
            agenda.put(Constants.AGENDA_URL_KEY, jsonUsersArray);

            return agenda;
        } catch (ParserConfigurationException | IOException | SAXException | JSONException e) {
            Log.e("e", "ERROR in XML FROM URL PARSER: " + e.toString());
        }

        return null;
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

    public static String getCurrenDate() {
        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return day + "/" + month + "/" + year;
    }
}
