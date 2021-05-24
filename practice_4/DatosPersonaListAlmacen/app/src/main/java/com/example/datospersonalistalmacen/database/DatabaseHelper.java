package com.example.datospersonalistalmacen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.datospersonalistalmacen.UnaPersona;
import com.example.datospersonalistalmacen.model.Almacen;
import com.example.datospersonalistalmacen.utils.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase database) {
        Log.d("d", "Creating database");
        createTable(database); // Create "users" table
        loadInitialData(database, Constants.CP_TABLE_NAME); // Load 5 records
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // void
    }

    private void createTable(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + Constants.CP_TABLE_NAME + " (" +
                Constants.ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.NAME_COLUMN + " TEXT, " +
                Constants.SURENNAMES_COLUMN + " TEXT, " +
                Constants.AGE_COLUMN + " TEXT, " +
                Constants.PHONE_COLUMN + " TEXT, " +
                Constants.DRIVING_LICENSE_COLUMN + " TEXT, " +
                Constants.ENGLISH_LEVEL_COLUMN + " TEXT, " +
                Constants.DATE_COLUMN + " TEXT);";
        database.execSQL(cmd);
    }


    private void loadInitialData(SQLiteDatabase database, String table) {
        Almacen storageData = new Almacen();

        // Insert data in database
        for (UnaPersona p:storageData.getList()) {
            ContentValues values = new ContentValues();

            values.put(Constants.NAME_COLUMN, p.getName());
            values.put(Constants.SURENNAMES_COLUMN, p.getSurename());
            values.put(Constants.AGE_COLUMN, p.getAge());
            values.put(Constants.PHONE_COLUMN, p.getPhone());
            values.put(Constants.DRIVING_LICENSE_COLUMN , String.valueOf(p.getHasDrivingLicense()));
            values.put(Constants.ENGLISH_LEVEL_COLUMN , p.getEnglishLevel());
            values.put(Constants.DATE_COLUMN , p.getStringDate());
            database.insert(table, null, values);
        }
    }
}
