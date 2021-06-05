package com.example.mascotas.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mascotas.interfaces.Constants;
import com.example.mascotas.interfaces.IOConstants;
import com.example.mascotas.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pet implements Parcelable {

    private int order;
    private String id;
    private String name;
    private String owner;
    private String type;
    private Date date;
    private boolean isVaccinated;

    public Pet() {
        this.order = -1;
        this.id = "";
        this.name = "";
        this.owner = "";
        this.type = "";
        this.date = null;
        this.isVaccinated = false;
    }

    public Pet(JSONObject objectFromURL) {

        try {
            this.id = objectFromURL.getString(IOConstants.JSON_URL_ID_PARAMETER);
        } catch (JSONException e) {
            this.id = Constants.UNKNOWN_DEFAULT_VALUE;
        }

        try {
            this.name = objectFromURL.getString(IOConstants.JSON_URL_NAME_PARAMETER);
        } catch (JSONException e) {
            this.name = Constants.UNKNOWN_DEFAULT_VALUE;
        }

        try {
            this.owner = objectFromURL.getString(IOConstants.JSON_URL_OWNER_PARAMETER);
        } catch (JSONException e) {
            this.owner = Constants.UNKNOWN_DEFAULT_VALUE;
        }

        try {
            switch (objectFromURL.getString(IOConstants.JSON_URL_TYPE_PARAMETER)){
                case "Gato":
                    this.type = Constants.CAT_VALUE;
                    break;
                case "Pájaro":
                    this.type = Constants.BIRD_VALUE;
                    break;
                case "Perro":
                    this.type = Constants.DOG_VALUE;
                    break;
                case "Otro":
                    this.type = Constants.OTHER_VALUE;
                    break;
                default:
                    this.type = Constants.OTHER_VALUE;
                    break;
            }
        } catch (JSONException e) {
            this.type = Constants.UNKNOWN_DEFAULT_VALUE;
        }

        try {
            this.isVaccinated = (objectFromURL.getString(IOConstants.JSON_URL_VACCINATED_PARAMETER) == "Si") ? true : false;
        } catch (JSONException e) {
            this.isVaccinated = false;
        }

        try {
            this.date = this.parseFromStringToDate(objectFromURL.getString(IOConstants.JSON_URL_ADOPTION_DATE_PARAMETER));
        } catch (JSONException e) {
            this.date = null;
        }
    }

    public Pet(String name, String owner, String type, Date date, boolean isVaccinated) {
        this.order = -1;
        this.id = "";
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.date = date;
        this.isVaccinated = isVaccinated;
    }

    public Pet(String name, String owner, String type, String date, boolean isVaccinated) {
        this.order = -1;
        this.id = "";
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.date = this.parseFromStringToDate(date);

        this.isVaccinated = isVaccinated;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isVaccinated() {
        return isVaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    /*********** PARCELABLE ************/

    protected Pet(Parcel in) {
        order = in.readInt();
        id = in.readString();
        name = in.readString();
        owner = in.readString();
        type = in.readString();
        date = (Date) in.readSerializable();
        isVaccinated = in.readByte() != 0;
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(order);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(owner);
        dest.writeString(type);
        dest.writeSerializable(date);
        dest.writeByte((byte) (isVaccinated ? 1 : 0));
    }

    /*********** PARCELABLE ************/
}
