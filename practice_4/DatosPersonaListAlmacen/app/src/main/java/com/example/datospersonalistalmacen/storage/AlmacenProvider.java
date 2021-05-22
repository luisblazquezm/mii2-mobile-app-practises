package com.example.datospersonalistalmacen.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class AlmacenProvider extends ContentProvider {

    // Defines a handle to the Room database
    private AppDatabase appDatabase;

    // Defines a Data Access Object to perform the database operations
    private UserDao userDao;

    // Defines the database name
    private static final String DBNAME = "mydb";

    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for table 3 are shown.
         */

        /*
         * Sets the integer value for multiple rows in table 3 to 1. Notice that no wildcard is used
         * in the path
         */
        uriMatcher.addURI("com.example.app.provider", "table3", 1);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.example.app.provider/table3/3" matches, but
         * "content://com.example.app.provider/table3 doesn't.
         */
        uriMatcher.addURI("com.example.app.provider", "table3/#", 2);
    }

    public boolean onCreate() {

        // Creates a new database object.
        appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, DBNAME).build();

        // Gets a Data Access Object to perform the database operations
        userDao = appDatabase.getUserDao();

        return true;
    }

    // Implements ContentProvider.query()
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        /*
         * Choose the table to query and a sort order based on the code returned for the incoming
         * URI. Here, too, only the statements for table 3 are shown.
         */
        switch (uriMatcher.match(uri)) {


            // If the incoming URI was for all of table3
            case 1:

                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;

            // If the incoming URI was for a single row
            case 2:

                /*
                 * Because this URI was for a single row, the _ID value part is
                 * present. Get the last path segment from the URI; this is the _ID value.
                 * Then, append the value to the WHERE clause for the query
                 */
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;

            default:
            ...
                // If the URI is not recognized, you should do some error handling here.
        }
        // call the code to actually do the query
    }

    // Implements the provider's insert method
    public Cursor insert(Uri uri, ContentValues values) {
        // Insert code here to determine which DAO to use when inserting data, handle error conditions, etc.

    }

    // Implements the provider's insert method
    public Cursor update(Uri uri, ContentValues values) {
        // Insert code here to determine which DAO to use when inserting data, handle error conditions, etc.

    }

    // Implements the provider's insert method
    public Cursor delete(Uri uri, ContentValues values) {
        // Insert code here to determine which DAO to use when inserting data, handle error conditions, etc.

    }
}


