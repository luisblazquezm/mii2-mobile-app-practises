package com.example.datospersonalistalmacen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.datospersonalistalmacen.UnaPersona;
import com.example.datospersonalistalmacen.constants.DBConstants;
import com.example.datospersonalistalmacen.model.Almacen;
import com.example.datospersonalistalmacen.constants.Constants;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private List<UnaPersona> users = null;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, List<UnaPersona> users) {
        super(context, name, factory, version);
        this.users = users;
    }

    public void onCreate(SQLiteDatabase database) {
        Log.d("d", "Creating database");
        createTable(database); // Create "users" table
        loadInitialData(database, DBConstants.CP_TABLE_NAME); // Load 5 records
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // void
    }

    private void createTable(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + DBConstants.CP_TABLE_NAME + " (" +
                DBConstants.ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.NAME_COLUMN + " TEXT, " +
                DBConstants.SURENNAMES_COLUMN + " TEXT, " +
                DBConstants.AGE_COLUMN + " TEXT, " +
                DBConstants.PHONE_COLUMN + " TEXT, " +
                DBConstants.DRIVING_LICENSE_COLUMN + " TEXT, " +
                DBConstants.ENGLISH_LEVEL_COLUMN + " TEXT, " +
                DBConstants.DATE_COLUMN + " TEXT);";
        database.execSQL(cmd);
    }


    private void loadInitialData(SQLiteDatabase database, String table) {

        // Insert data in database
        for (UnaPersona p:this.users) {
            ContentValues values = new ContentValues();

            values.put(DBConstants.NAME_COLUMN, p.getName());
            values.put(DBConstants.SURENNAMES_COLUMN, p.getSurename());
            values.put(DBConstants.AGE_COLUMN, p.getAge());
            values.put(DBConstants.PHONE_COLUMN, p.getPhone());
            values.put(DBConstants.DRIVING_LICENSE_COLUMN , String.valueOf(p.getHasDrivingLicense()));
            values.put(DBConstants.ENGLISH_LEVEL_COLUMN , p.getEnglishLevel());
            values.put(DBConstants.DATE_COLUMN , p.getStringDate());
            database.insert(table, null, values);
        }
    }
}
