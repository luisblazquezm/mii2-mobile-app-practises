package com.example.datospersonalistalmacen;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UnaPersona implements Serializable {

    String name;
    String surename;
    String age;
    String phone;
    boolean hasDrivingLicense;
    String englishLevel;
    Date date;

    public UnaPersona() {
        this.name = "";
        this.surename = "";
        this.age = "";
        this.phone = "";
        this.hasDrivingLicense = false;
        this.englishLevel = "";
        this.date = null;
    }

    public UnaPersona(String name, String surename,
                      String age, String phone,
                      boolean hasDrivingLicense, String englishLevel, Date date) {
        this.name = name;
        this.surename = surename;
        this.age = age;
        this.phone = phone;
        this.hasDrivingLicense = hasDrivingLicense;
        this.englishLevel = englishLevel;
        this.date = date;
    }

    public UnaPersona(String name, String surename,
                      String age, String phone,
                      boolean hasDrivingLicense, String englishLevel, String date) {
        this.name = name;
        this.surename = surename;
        this.age = age;
        this.phone = phone;
        this.hasDrivingLicense = hasDrivingLicense;
        this.englishLevel = englishLevel;
        this.date = this.parseFromStringToDate(date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurename() {
        return surename;
    }

    public void setSurename(String surename) {
        this.surename = surename;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getHasDrivingLicense() {
        return hasDrivingLicense;
    }

    public void setHasDrivingLicense(boolean hasDrivingLicense) {
        this.hasDrivingLicense = hasDrivingLicense;
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStringDate() {
        return this.parseFromDateToString(date);
    }

    public String parseFromDateToString(Date d) {
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);

        return df.format(d);
    }

    public Date parseFromStringToDate(String stringDate) {
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(stringDate);
        } catch (ParseException e) {
            return null;
        }
    }

}
