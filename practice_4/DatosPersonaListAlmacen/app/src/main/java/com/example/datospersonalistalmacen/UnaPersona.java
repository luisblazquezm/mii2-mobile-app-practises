package com.example.datospersonalistalmacen;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.datospersonalistalmacen.constants.Constants;
import com.example.datospersonalistalmacen.constants.IOConstants;
import com.example.datospersonalistalmacen.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UnaPersona implements Parcelable {

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

    public UnaPersona(JSONObject objectFromURL) {
        try {
            this.name = objectFromURL.getString(IOConstants.JSON_URL_NAME_PARAMETER);
        } catch (JSONException e) {
            this.name = Constants.UNKNOWN_DEFAULT_VALUE;
        }

        try {
            this.surename = objectFromURL.getString(IOConstants.JSON_URL_SURENAMES_PARAMETER);
        } catch (JSONException e) {
            this.surename = Constants.UNKNOWN_DEFAULT_VALUE;
        }

        try {
            this.phone = objectFromURL.getString(IOConstants.JSON_URL_PHONE_PARAMETER);
        } catch (JSONException e) {
            this.phone = Constants.UNKNOWN_DEFAULT_VALUE;
        }

        try {
            this.hasDrivingLicense = (objectFromURL.getString(IOConstants.JSON_URL_DRIVING_LICENSE_PARAMETER) == "S") ? true : false;
        } catch (JSONException e) {
            this.hasDrivingLicense = false;
        }

        try {
            switch (objectFromURL.getString(IOConstants.JSON_URL_ENGLISH_LEVEL_PARAMETER)){
                case "H":
                    this.englishLevel = Constants.HIGH_LEVEL_VALUE;
                    break;
                case "M":
                    this.englishLevel = Constants.MEDIUM_LEVEL_VALUE;
                    break;
                case "L":
                    this.englishLevel = Constants.LOW_LEVEL_VALUE;
                    break;
                default:
                    this.englishLevel = Constants.HIGH_LEVEL_VALUE;
                    break;
            }
        } catch (JSONException e) {
            this.englishLevel = Constants.UNKNOWN_DEFAULT_VALUE;
        }

        try {
            this.date = this.parseFromStringToDate(objectFromURL.getString(IOConstants.JSON_URL_REGISTRY_DATE_PARAMETER));
        } catch (JSONException e) {
            this.date = null;
        }

        this.age = Constants.UNKNOWN_DEFAULT_VALUE;
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

        if (d == null)
            return Utils.getCurrenDate();
        else
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

    /*********** PARCELABLE ************/

    protected UnaPersona(Parcel in) {
        name = in.readString();
        surename = in.readString();
        age = in.readString();
        phone = in.readString();
        hasDrivingLicense = in.readByte() != 0;
        englishLevel = in.readString();
    }

    public static final Creator<UnaPersona> CREATOR = new Creator<UnaPersona>() {
        @Override
        public UnaPersona createFromParcel(Parcel in) {
            return new UnaPersona(in);
        }

        @Override
        public UnaPersona[] newArray(int size) {
            return new UnaPersona[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surename);
        dest.writeString(age);
        dest.writeString(phone);
        dest.writeByte((byte) (hasDrivingLicense ? 1 : 0));
        dest.writeString(englishLevel);
    }

    /*********** PARCELABLE ************/
}
