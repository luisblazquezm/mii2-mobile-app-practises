package com.example.mascotas.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.mascotas.interfaces.IOConstants;
import com.example.mascotas.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PetModel implements Parcelable {

    // LIST OF MODEL
    private ArrayList<Pet> petList = null;

    public PetModel() {
        // Set variables
        this.petList = new ArrayList<Pet>();
    }

    public void initializeStaticStorage() {
        this.petList.add(new Pet());
        this.petList.add(new Pet());
        this.petList.add(new Pet());
    }

    public void loadFromURL(String contentURL) {
        try {
            // Retrieve content from URL as bytes
            URL url = new URL(contentURL);

            JSONObject resultJSON = null;
            JSONArray pets = new JSONArray();
            if (contentURL.endsWith(".txt") || contentURL.endsWith(".json")) {
                // Retrieve from URL
                resultJSON = Utils.extractJSONFromURL(url);

                if (null == resultJSON) {
                    this.initializeStaticStorage(); // ERROR ocurred, load from static
                } else {
                    Log.d("d", "resultJSON JSON: " + resultJSON.toString());

                    // Get users from JSON content
                    pets = resultJSON.getJSONArray("");
                }
            }

            // Add people from list of url
            for(int i = 0; i <pets.length(); i++) {
                this.petList.add(new Pet(pets.getJSONObject(i)));
            }

        } catch (MalformedURLException mue) {
            this.initializeStaticStorage(); // ERROR ocurred, load from static
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (JSONException e) {
            this.initializeStaticStorage(); // ERROR ocurred, load from static
        }
    }

    public ArrayList<Pet> getList() {
        return this.petList;
    }

    public void setPeopleList(ArrayList<Pet> petList) {
        this.petList = petList;
    }

    public void insert(Pet newPet) {
        this.petList.add(newPet);
    }

    public void update(int position, Pet petModified) {
        this.petList.set(position, petModified);
    }

    public void delete(Pet petToDelete) {
        this.petList.remove(petToDelete);
    }

    /*********** PARCELABLE ************/
    protected PetModel(Parcel in) {
        petList = in.createTypedArrayList(Pet.CREATOR);
    }

    public static final Creator<PetModel> CREATOR = new Creator<PetModel>() {
        @Override
        public PetModel createFromParcel(Parcel in) {
            return new PetModel(in);
        }

        @Override
        public PetModel[] newArray(int size) {
            return new PetModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(petList);
    }
    /*********** PARCELABLE ************/

}
