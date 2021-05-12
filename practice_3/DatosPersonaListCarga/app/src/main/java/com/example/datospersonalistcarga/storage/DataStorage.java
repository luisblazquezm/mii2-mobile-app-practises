package com.example.datospersonalistcarga.storage;

import com.example.datospersonalistcarga.UnaPersona;

import java.util.ArrayList;

public class DataStorage {

    public static ArrayList<UnaPersona> loadFromStorage(){

        ArrayList<UnaPersona> peopleList = new ArrayList<>();

        peopleList.add(new UnaPersona("Luis", "Blázquez Miñambres", "23",
                "34343434", true,
                "ALTO", "23/04/2021"));
        peopleList.add(new UnaPersona("Manuel", "Pérez Pérez", "53",
                "45454545", true,
                "MEDIO", "12/04/2021"));
        peopleList.add(new UnaPersona("Marcos", "Rodriguez Pérez", "45",
                "999999999", false,
                "BAJO", "13/04/2021"));

        return peopleList;
    }

}
