package com.example.mascotas.models;

import android.util.Log;

import com.example.mascotas.interfaces.IOConstants;
import com.example.mascotas.utils.Utils;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PetUrlLoader {

    public ArrayList<Pet> loadFromURL(String contentURL, String type) {
        ArrayList<Pet> petList = new ArrayList<Pet>();

        try {
            // Retrieve content from URL as bytes
            URL url = new URL(contentURL);

            // Choose type of file
            switch (type){
                case IOConstants.JSON_TYPE_FILE:
                    petList = this.loadDataFromJSON(url);
                case IOConstants.XML_TYPE_FILE:
                    petList = this.loadDataFromXML(url);
                case IOConstants.CSV_TYPE_FILE:
                    petList = this.loadDataFromCSV(url);
                default:
                    petList = this.loadDataFromJSON(url);
            }
        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
            return null; // ERROR ocurred, load from static
        }

        return petList;
    }

    private ArrayList<Pet> loadDataFromJSON(URL url) {
        PetJSON[] resultJSON = null;
        JSONArray pets = new JSONArray();
        ArrayList<Pet> petList = new ArrayList<Pet>();

        // Retrieve from URL
        resultJSON = (PetJSON[]) Utils.extractJSONFromURL(url);

        if (null == resultJSON) {
            return null;
        } else {
            Log.d("d", "resultJSON JSON: " + resultJSON.toString());

            // Add people from list of url
            for(int i = 0; i < resultJSON.length; i++) {
                petList.add(resultJSON[i].parseToPet());
            }
        }

        return petList;
    }

    private ArrayList<Pet> loadDataFromXML(URL url) {
        return null;
    }

    private ArrayList<Pet> loadDataFromCSV(URL url) {
        return null;
    }
}
