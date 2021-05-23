package com.example.datospersonalistalmacen.model;

import android.content.ContentValues;
import android.net.Uri;

import com.example.datospersonalistalmacen.UnaPersona;
import com.example.datospersonalistalmacen.utils.Constants;

import java.net.URI;
import java.util.ArrayList;

public class Almacen {

    private ArrayList<UnaPersona> peopleList = new ArrayList<UnaPersona>();
    private AlmacenProvider contentProviderManager = new AlmacenProvider();

    public Almacen() {
        initializeStorage();
    }

    private void initializeStorage() {
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

            // Check if record already exists by phone
            String phone = p.getPhone();
            if (contentProviderManager.checkIfRecordExistsByPhone(phone))
                continue;

            // Insert bulk all the record
            ContentValues values = new ContentValues();
            values.put(Constants.NAME_COLUMN, p.getName());
            values.put(Constants.SURENNAMES_COLUMN, p.getSurename());
            values.put(Constants.AGE_COLUMN, p.getAge());
            values.put(Constants.PHONE_COLUMN, p.getPhone());
            values.put(Constants.DRIVING_LICENSE_COLUMN, String.valueOf(p.getHasDrivingLicense()));
            values.put(Constants.ENGLISH_LEVEL_COLUMN, p.getEnglishLevel());
            values.put(Constants.DATE_COLUMN, p.getStringDate());
            contentProviderManager.insert(AlmacenProvider.CONTENT_URI, values);
        }

    }
}
