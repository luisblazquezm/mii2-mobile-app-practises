package com.example.datospersonalistalmacen.model;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.datospersonalistalmacen.UnaPersona;
import com.example.datospersonalistalmacen.utils.Constants;
import com.example.datospersonalistalmacen.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Almacen implements Parcelable {

    private ArrayList<UnaPersona> peopleList = null;
    private AlmacenProvider contentProviderManager = null;

    public Almacen() {

        // Set variables
        this.peopleList = new ArrayList<UnaPersona>();
        this.contentProviderManager = new AlmacenProvider();
    }

    public void initializeStaticStorage() {
        this.peopleList.add(new UnaPersona("Luis", "Blázquez Miñambres", "23",
                "34343434", true,
                "ALTO", "23/04/2021"));
        this.peopleList.add(new UnaPersona("Manuel", "Pérez Pérez", "53",
                "45454545", true,
                "MEDIO", "12/04/2021"));
        this.peopleList.add(new UnaPersona("Marcos", "Rodriguez Pérez", "45",
                "999999999", false,
                "BAJO", "13/04/2021"));
    }

    public void initializeStorageFromURL(String contentURL) {
        try {
            // Retrieve content from URL as bytes
            URL url = new URL(contentURL);

            JSONObject resultJSON = null;
            JSONArray users = new JSONArray();
            if (contentURL.endsWith(".txt") || contentURL.endsWith(".json")) {
                // Retrieve from URL
                resultJSON = Utils.extractJSONFromURL(url);

                if (null == resultJSON) {
                    this.initializeStaticStorage(); // ERROR ocurred, load from static
                } else {
                    Log.d("d", "resultJSON JSON: " + resultJSON.toString());

                    // Get users from JSON content
                    users = resultJSON.getJSONArray(Constants.AGENDA_URL_KEY);
                }
            } else if (contentURL.endsWith(".xml")) {
                // Retrieve from URL as JSON (XML is parsed to JSON because it is an structure
                // easier to work with
                resultJSON = Utils.extractXMLFromURL(url);

                if (null == resultJSON) {
                    this.initializeStaticStorage(); // ERROR ocurred, load from static
                } else {
                    Log.d("d", "resultJSON XML: " + resultJSON.toString());

                    // Get users from XML content as JSON
                    users = resultJSON.getJSONArray(Constants.AGENDA_URL_KEY);
                }
            }

            // Add people from list of url
            for(int i = 0; i <users.length(); i++) {
                this.peopleList.add(new UnaPersona(users.getJSONObject(i)));
            }

        } catch (MalformedURLException mue) {
            this.initializeStaticStorage(); // ERROR ocurred, load from static
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (JSONException e) {
            this.initializeStaticStorage(); // ERROR ocurred, load from static
        }
    }

    public ArrayList<UnaPersona> getList(){
        return this.peopleList;
    }

    public void setPeopleList(ArrayList<UnaPersona> peopleList) {
        this.peopleList = peopleList;
    }

    public void insert(UnaPersona newPerson) {
        this.peopleList.add(newPerson);
    }

    public void update(int position, UnaPersona personModified) {
        this.peopleList.set(position, personModified);
    }

    public void delete(UnaPersona personToDelete) {
        this.peopleList.remove(personToDelete);
    }

    public void bulkDataToContentProvider() {
        for (UnaPersona p : this.peopleList) {

            // Check if record already exists by phone. NOT NECESSARY
            /*String phone = p.getPhone();
            if (contentProviderManager.checkIfRecordExistsByPhone(phone))
                continue;
             */

            // Insert bulk all the record
            ContentValues values = new ContentValues();
            values.put(Constants.NAME_COLUMN, p.getName());
            values.put(Constants.SURENNAMES_COLUMN, p.getSurename());
            values.put(Constants.AGE_COLUMN, p.getAge());
            values.put(Constants.PHONE_COLUMN, p.getPhone());
            values.put(Constants.DRIVING_LICENSE_COLUMN, String.valueOf(p.getHasDrivingLicense()));
            values.put(Constants.ENGLISH_LEVEL_COLUMN, p.getEnglishLevel());
            values.put(Constants.DATE_COLUMN, p.getStringDate());
            contentProviderManager.insert(contentProviderManager.CONTENT_URI, values);
        }

    }

    /*********** PARCELABLE ************/
    protected Almacen(Parcel in) {
        peopleList = in.createTypedArrayList(UnaPersona.CREATOR);
    }

    public static final Creator<Almacen> CREATOR = new Creator<Almacen>() {
        @Override
        public Almacen createFromParcel(Parcel in) {
            return new Almacen(in);
        }

        @Override
        public Almacen[] newArray(int size) {
            return new Almacen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(peopleList);
    }
    /*********** PARCELABLE ************/
}
