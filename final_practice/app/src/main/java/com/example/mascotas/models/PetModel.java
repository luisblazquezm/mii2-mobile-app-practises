package com.example.mascotas.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.mascotas.activities.PetListActivity;
import com.example.mascotas.interfaces.Constants;
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
    private ArrayList<Pet> petList;
    private ArrayList<Pet> pendingPetsList;

    // OTHER
    private int numTotalProcessed;
    private int numTotalEvents;
    private static PetModel modelInstance;
    private PetUrlLoader petLoader = null;

    public PetModel() {
        // Set variables
        this.petList = new ArrayList<Pet>();
        this.pendingPetsList = new ArrayList<Pet>();
        this.numTotalEvents = 0;
        this.numTotalProcessed = 0;
        this.petLoader = new PetUrlLoader();
    }

    public static PetModel retrieveInstance(){
        if(PetModel.modelInstance == null){
            PetModel.modelInstance = new PetModel();
        }
        return PetModel.modelInstance;
    }

    public void initializeStaticStorage() {
        this.petList.add(new Pet("Peter", "Luis Felipe", Constants.DOG_VALUE,
                Utils.getCurrenDate(), false));
        this.petList.add(new Pet("Dorian", "Rafael", Constants.BIRD_VALUE,
                Utils.getCurrenDate(), true));
        this.petList.add(new Pet("Lucy", "Lucía", Constants.CAT_VALUE,
                Utils.getCurrenDate(), true));
    }

    public void initializeStorageFromUrl() {
        this.petList = this.petLoader.loadFromURL(Constants.DEFAULT_JSON_PET_URL,IOConstants.JSON_TYPE_FILE);
    }

    /*********** CRUD OPERATIONS ************/

    public void insert(Pet newPet) {
        this.petList.add(newPet);
        this.numTotalEvents++;
    }

    public void insertScraped(Pet newPet) {
        this.petList.add(newPet);
    }

    public void insertAllScraped(ArrayList<Pet> scrapedPets) {
        this.petList.addAll(scrapedPets);
    }

    public void update(int position, Pet petModified) {
        this.petList.set(position, petModified);
    }

    public void delete(Pet petToDelete) {
        this.petList.remove(petToDelete);
        this.numTotalEvents--;
    }

    public void insertPending(Pet pendingPet) {
        this.pendingPetsList.add(pendingPet);
        this.numTotalEvents++;
    }

    public void updatePending(int position, Pet pendingPetModified) {
        this.pendingPetsList.set(position, pendingPetModified);
    }

    public void deletePending(Pet pendingPetToDelete) {
        this.pendingPetsList.remove(pendingPetToDelete);
        this.numTotalEvents--;
    }

    public Pet getFirstPetFromURL() {
        return this.petList.get(0);
    }

    /*********** NOTIFICATIONS OPERATIONS ************/

    public void processPendingPet(){
        // Add new processed pet data
        this.numTotalProcessed += this.pendingPetsList.size();
        Log.d("d", "Peding new: " + this.pendingPetsList.size());
        Log.d("d", "List BEFORE adding pending: " + this.petList.size());
        this.petList.addAll(this.pendingPetsList);
        Log.d("d", "List AFTER adding pending: " + this.petList.size());

        // Clear pending list
        this.pendingPetsList.clear();
    }

    public void clearPendingPets(){
        this.pendingPetsList.clear();
    }

    /*********** GETTER AND SETTERS ************/

    public ArrayList<Pet> getPetList() {
        return this.petList;
    }

    public void setPetList(ArrayList<Pet> petList) {
        this.petList = petList;
    }

    public ArrayList<Pet> getPendingPetsList() {
        return this.pendingPetsList;
    }

    public void setPendingPetsList(ArrayList<Pet> pendingPetsList) {
        this.pendingPetsList = pendingPetsList;
    }

    public int getNumTotalProcessed() {
        return this.numTotalProcessed;
    }

    public void setNumTotalProcessed(int numTotalProcessed) {
        this.numTotalProcessed = numTotalProcessed;
    }

    public int getNumTotalEvents() {
        return this.numTotalEvents;
    }

    public void setNumTotalEvents(int numTotalEvents) {
        this.numTotalEvents = numTotalEvents;
    }

    public static PetModel getModelInstance() {
        return modelInstance;
    }

    public static void setModelInstance(PetModel modelInstance) {
        PetModel.modelInstance = modelInstance;
    }

    /*********** PARCELABLE ************/
    protected PetModel(Parcel in) {
        petList = in.createTypedArrayList(Pet.CREATOR);
        pendingPetsList = in.createTypedArrayList(Pet.CREATOR);
        numTotalEvents = in.readInt();
        numTotalProcessed = in.readInt();
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
        dest.writeTypedList(this.petList);
        dest.writeTypedList(this.pendingPetsList);
        dest.writeInt(this.numTotalEvents);
        dest.writeInt(this.numTotalProcessed);
    }

}
