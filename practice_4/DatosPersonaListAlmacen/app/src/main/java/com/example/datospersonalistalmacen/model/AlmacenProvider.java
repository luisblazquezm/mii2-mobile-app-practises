package com.example.datospersonalistalmacen.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.example.datospersonalistalmacen.UnaPersona;
import com.example.datospersonalistalmacen.constants.DBConstants;
import com.example.datospersonalistalmacen.database.DatabaseHelper;
import com.example.datospersonalistalmacen.constants.Constants;

import java.util.List;

public class AlmacenProvider extends ContentProvider {

    // CONSTANTS
    private final static String AUTHORITY = "net.luisblazquezm"; /* Content Provider Authority */
    public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DBConstants.CP_TABLE_NAME); /* URI of the main content */
    private final static String SINGLE_MIME = "vnd.android.cursor.item/vnd." + AUTHORITY + DBConstants.CP_TABLE_NAME; /* Returns query for just one row*/
    private final static String MULTIPLE_MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY + DBConstants.CP_TABLE_NAME;/* Returns query for multiple rows*/
    private final static int ALLROWS = 1; /* Database return Code used for URI in mulitple records */
    private final static int SINGLE_ROW = 2; /* Database return Code used for URI in one records */

    // OTHER
    private DatabaseHelper dbManager; // Defines a handle to the Room database
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); // Creates a UriMatcher object.

    // URI assignation
    static {
        uriMatcher.addURI(AUTHORITY, DBConstants.CP_TABLE_NAME, ALLROWS);
        uriMatcher.addURI(AUTHORITY, DBConstants.CP_TABLE_NAME + "/#", SINGLE_ROW);
    }

    public AlmacenProvider(Context context, List<UnaPersona> users)
    {
        this.dbManager = new DatabaseHelper(context, DBConstants.CP_DATABASE_NAME, null, DBConstants.CP_DATABASE_VERSION, users);
    }

    public AlmacenProvider()
    {
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public String getType(Uri uri) {
        /*
         * Check if URI coming to content provider is referenced to one record query
         * or multiple records query and return related database MIME code to make the request
         */
        switch (this.uriMatcher.match(uri)) {
            case AlmacenProvider.ALLROWS:
                return this.MULTIPLE_MIME;
            case AlmacenProvider.SINGLE_ROW:
                return this.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Unknown type of activity: " + uri);
        }
    }

    private boolean checkValues(ContentValues values) {

        // Check name
        if (values.containsKey(DBConstants.NAME_COLUMN)){
            String name = values.getAsString(DBConstants.NAME_COLUMN);
            if (null == name) {
                //throw new IllegalArgumentException("Name cannot be set as null and must be a string");
                return false;
            }
        }

        // Check surenames
        if (values.containsKey(DBConstants.SURENNAMES_COLUMN)){
            String surenames = values.getAsString(DBConstants.SURENNAMES_COLUMN);
            if (null == surenames) {
                //throw new IllegalArgumentException("Surenames cannot be set as null and must be a string");
                return false;
            }
        }

        // Check age
        if (values.containsKey(DBConstants.AGE_COLUMN)){
            String age = values.getAsString(DBConstants.AGE_COLUMN);
            if (null == age) {
                //throw new IllegalArgumentException("Age cannot be set as null and must be a string");
                return false;
            }
        }

        // Check phone
        if (values.containsKey(DBConstants.PHONE_COLUMN)){
            String phone = values.getAsString(DBConstants.PHONE_COLUMN);
            if (null == phone) {
                //throw new IllegalArgumentException("Phone cannot be set as null and must be a string");
                return false;
            }
        }

        // Check driving license
        if (values.containsKey(DBConstants.DRIVING_LICENSE_COLUMN)){
            String drivingLicense = values.getAsString(DBConstants.DRIVING_LICENSE_COLUMN);
            if (null == drivingLicense) {
                //throw new IllegalArgumentException("Driving License cannot be set as null and must be a string");
                return false;
            }
        }

        // Check english level
        if (values.containsKey(DBConstants.ENGLISH_LEVEL_COLUMN)){
            String englishLevel = values.getAsString(DBConstants.ENGLISH_LEVEL_COLUMN);
            if (null == englishLevel) {
                //throw new IllegalArgumentException("English Level cannot be set as null and must be a string");
                return false;
            }
        }

        // Check date
        if (values.containsKey(DBConstants.DATE_COLUMN)){
            String date = values.getAsString(DBConstants.DATE_COLUMN);
            if (null == date) {
                return false;
            }
        }

        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbManager.getWritableDatabase(); // Obtain base de datos
        int match = this.uriMatcher.match(uri); // Compare incoming Uri
        Cursor c;

        // Choose the table to query and a sort order based on the code returned for the incoming URI.
        switch (match) {
            case AlmacenProvider.ALLROWS: // Querying all the records
                c = db.query(DBConstants.CP_TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), AlmacenProvider.CONTENT_URI);
                break;
            case AlmacenProvider.SINGLE_ROW: // Querying just one record based on URI id
                long userID = ContentUris.parseId(uri);
                c = db.query(DBConstants.CP_TABLE_NAME, projection,
                        DBConstants.ID_COLUMN + " = " + userID,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), AlmacenProvider.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("Not supported URI: " + uri);
        }

        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        ContentValues contentValues = null;

        // Validate URI
        if (this.uriMatcher.match(uri) != AlmacenProvider.ALLROWS)
            throw new IllegalArgumentException("Unknown URI: " + uri);

        // Set new values
        if (values != null)
            contentValues = new ContentValues(values);
        else
            contentValues = new ContentValues();

        // Verify values. Not necessary because each one has a different id
        /*boolean valuesCorrectlyFormed = this.checkValues(values);
        if (!valuesCorrectlyFormed)
            return null;*/

        // Insert one record
        SQLiteDatabase db = this.dbManager.getWritableDatabase();
        long rowId = db.insert(DBConstants.CP_TABLE_NAME,null, contentValues);

        // Check if insertion was correctly made
        if (rowId > 0) {
            Uri resultingUserURI = ContentUris.withAppendedId(this.CONTENT_URI, rowId);
            //getContext().getContentResolver().notifyChange(resultingUserURI, null); // Notify change to context
            return resultingUserURI;
        } else {
            throw new SQLException("Error while inserting record in : " + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbManager.getWritableDatabase();
        int rowIDUpdated;

        switch (this.uriMatcher.match(uri)) {
            case AlmacenProvider.ALLROWS:
                rowIDUpdated = db.update(DBConstants.CP_TABLE_NAME, values, selection, selectionArgs);
                break;
            case AlmacenProvider.SINGLE_ROW:
                String userID = uri.getPathSegments().get(1);
                String updateQueryClause = DBConstants.ID_COLUMN + "=" + userID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");

                rowIDUpdated = db.update(DBConstants.CP_TABLE_NAME, values, updateQueryClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }

        //getContext().getContentResolver().notifyChange(uri, null); // Notify change to context

        return rowIDUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbManager.getWritableDatabase();
        int match = this.uriMatcher.match(uri);
        int rowIDDeleted;

        switch (match) {
            case AlmacenProvider.ALLROWS:
                rowIDDeleted = db.delete(DBConstants.CP_TABLE_NAME, selection, selectionArgs);
                break;
            case AlmacenProvider.SINGLE_ROW:
                long userID = ContentUris.parseId(uri);
                String deleteQueryClause = DBConstants.ID_COLUMN + "=" + userID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");

                rowIDDeleted = db.delete(DBConstants.CP_TABLE_NAME, deleteQueryClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown user row: " + uri);
        }

        //getContext().getContentResolver().notifyChange(uri, null); // Notify change to context
        return rowIDDeleted;
    }

    public boolean checkIfRecordExistsByPhone(String phoneToSearch) {
        String[] selectionArgs = {""};
        String selectionClause, sortOrder = "";

        // Columns to return in the query
        String[] projection =
        {
                DBConstants.ID_COLUMN
        };

        // Constructs a selection clause that matches the word that the user entered.
        selectionClause = DBConstants.PHONE_COLUMN + " = ?";

        // Moves the user's input string to the selection arguments.
        selectionArgs[0] = phoneToSearch;

        // Order by id
        sortOrder = "ORDER BY " + DBConstants.ID_COLUMN;

        // Does a query against the table and returns a Cursor object
        Cursor mCursor = this.query(this.CONTENT_URI,  // The content URI of the words table
                                    projection,        // The columns to return for each row
                                    selectionClause,  // Either null, or the word the user entered
                                    selectionArgs,    // Either empty, or the string the user entered
                                    sortOrder);       // The sort order for the returned rows

        // Some providers return null if an error occurs, others throw an exception
        if (null == mCursor) {
            return false;
        } else if (mCursor.getCount() < 1) { // If the Cursor is empty, the provider found no matches
            return false;
        } else { // Insert code here to do something with the results
            return true;
        }
    }

}


