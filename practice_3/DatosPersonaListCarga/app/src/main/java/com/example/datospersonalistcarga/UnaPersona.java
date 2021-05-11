package com.example.datospersonalistcarga;

import java.io.Serializable;

public class UnaPersona implements Serializable {

    String name;
    String surename;
    String age;
    String phone;
    boolean hasDrivingLicense;
    String englishLevel;
    String date;

    public UnaPersona() {
        this.name = "";
        this.surename = "";
        this.age = "";
        this.phone = "";
        this.hasDrivingLicense = false;
        this.englishLevel = "";
        this.date = "";
    }

    public UnaPersona(String name, String surename, String age, String phone, boolean hasDrivingLicense, String englishLevel, String date) {
        this.name = name;
        this.surename = surename;
        this.age = age;
        this.phone = phone;
        this.hasDrivingLicense = hasDrivingLicense;
        this.englishLevel = englishLevel;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
