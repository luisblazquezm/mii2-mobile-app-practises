package com.example.datospersonalistalmacen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.datospersonalistalmacen.model.Almacen;
import com.example.datospersonalistalmacen.utils.Constants;
import com.example.datospersonalistalmacen.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class DatosListActivity extends AppCompatActivity {

    // UI ELEMENTS
    private ListView listView = null;
    private Button addButton = null;
    private Button cancelButton = null;
    private Button startAppButton = null;
    private TextView emptyTextView = null;
    private ProgressBar loadingProgressBarr = null;

    // CONSTANTS
    private final int LOAD_TIME_IN_MILLISECONDS = 5 * 1000; // 5 seconds

    // OTHERS
    private Almacen userStorage = null;
    private AdapterPerson listViewArrayAdapter = null;
    private SharedPreferences sharedSettings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_list);

        // Initialize storage
        this.userStorage = new Almacen();

        // Get reference to shared settings
        this.sharedSettings = DatosListActivity.this.getSharedPreferences(Constants.SHARED_SETTINGS_KEY, Context.MODE_PRIVATE);

        // check if there is data from previous session

        // recovering the instance state
        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(Constants.PEOPLE_LIST_STATE_KEY);
            listView.onRestoreInstanceState(state);
        }

        // Capture the layout's TextView and set the string as its text
        this.listView = (ListView) findViewById(R.id.peopleListView);
        this.emptyTextView =(TextView) findViewById(R.id.emptyTextView);

        // Set empty message of "Not available" when list view is empty in the initialization
        this.listView.setEmptyView(emptyTextView);

        // Start app when start activity
        this.startApp();
    }

    private void startApp() {

        // Visible buttons and hide start app
        this.addButton = (Button) findViewById(R.id.addPersonButton);
        this.cancelButton = (Button) findViewById(R.id.exitButton);
        this.startAppButton = (Button) findViewById(R.id.startProgramButton);
        this.loadingProgressBarr = (ProgressBar) findViewById(R.id.progressBarLoading);

        this.addButton.setVisibility(View.VISIBLE);
        this.cancelButton.setVisibility(View.VISIBLE);
        this.loadingProgressBarr.setVisibility(View.VISIBLE);
        this.startAppButton.setVisibility(View.GONE);
        this.emptyTextView.setVisibility(View.GONE);

        // Fill listview with a thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Simulate loading
                try {
                    Thread.sleep(LOAD_TIME_IN_MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Hide loading progress barr
                        loadingProgressBarr.setVisibility(View.GONE);

                        // This is the array adapter, it takes the context of the activity as a
                        // first parameter, the type of list view as a second parameter and the
                        // array as a third parameter.
                        listViewArrayAdapter = new AdapterPerson(DatosListActivity.this, DatosListActivity.this.userStorage.getList());

                        listView.setAdapter(listViewArrayAdapter); // set adapter
                        listView.setOnItemClickListener(modifyPersonData); // set event when click on element
                        listView.setOnItemLongClickListener(deletePersonData); // set event when long click on element
                    }
                });
            }
        }).start();
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Parcelable state = (Parcelable) savedInstanceState.getSerializable(Constants.PEOPLE_LIST_STATE_KEY);
        listView.onRestoreInstanceState(state);
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.PEOPLE_LIST_STATE_KEY, this.listView.onSaveInstanceState());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check the type of operation requested by request code sent in startActivityForResult
        if (requestCode == Constants.LAUNCH_SECOND_ACTIVITY_TO_ADD) {
            if(resultCode == Activity.RESULT_OK) { // new person added
                // Get the Intent that started this activity and extract the string
                if (data != null) {
                    // Get extra attributes
                    UnaPersona newPerson = (UnaPersona) data.getSerializableExtra(Constants.INTENT_ELEMENT_NEW_PERSON_KEY);

                    // Add new person
                    userStorage.insert(newPerson);

                    // Notify adapter about the modification to update the data in list
                    this.listViewArrayAdapter.notifyDataSetChanged();
                }
            } else if (resultCode != Activity.RESULT_CANCELED) { // canceled
                // Nothing
            }
        } else if (requestCode == Constants.LAUNCH_SECOND_ACTIVITY_TO_MODIFY) {
            if(resultCode == Activity.RESULT_OK) { // new person added
                // Get the Intent that started this activity and extract the string
                if (data != null) {
                    // Get extra attributes
                    int position = data.getIntExtra(Constants.INTENT_ELEMENT_POSITION_TO_MODIFY_KEY, 0);
                    UnaPersona personModified = (UnaPersona) data.getSerializableExtra(Constants.INTENT_ELEMENT_NEW_PERSON_KEY);

                    // Set new data
                    userStorage.update(position, personModified);

                    // Notify adapter about the modification to update the data in list
                    this.listViewArrayAdapter.notifyDataSetChanged();
                }
            } else if (resultCode != Activity.RESULT_CANCELED) { // canceled
                // Nothing
            }
        }
    }

    public void addPersonData(View view) {
        Intent intent = new Intent(this, LanzaActividad.class);
        intent.putExtra(Constants.INTENT_REQUEST_CODE_KEY, Constants.LAUNCH_SECOND_ACTIVITY_TO_ADD); // add request code to identify operation in activity
        startActivityForResult(intent, Constants.LAUNCH_SECOND_ACTIVITY_TO_ADD);
    }

    public void storePersonData(View view) {
        String content = null;

        /* CHOOSE STORAGE TYPE */
        int storageTypeIdSelected = this.sharedSettings.getInt(Constants.FORMAT_SETTINGS_KEY, R.id.xmlFormatRadioButton);
        if (R.id.contentProviderRadioButton == storageTypeIdSelected){
            userStorage.bulkDataToContentProvider(); // Bulk all the records of the list into database with content provider
        } else if (R.id.externalMemoryRadioButton == storageTypeIdSelected) {

            /* CHOOSE FORMAT TYPE */
            int formatTypeIdSelected = this.sharedSettings.getInt(Constants.FORMAT_SETTINGS_KEY, R.id.xmlFormatRadioButton);
            if (R.id.xmlFormatRadioButton == formatTypeIdSelected) { // XML format selected in settings
                content = Utils.multipleItemsToXML(this.userStorage.getList()); // Parse to JSON object string
            } else if (R.id.jsonFormatRadioButton == formatTypeIdSelected){ // JSON format selected in settings
                content = Utils.toJSON(this.userStorage.getList()); // Parse to JSON object string
            }

            // Parse to bytes for better output export
            byte[] dataToExport = content.getBytes();

            // Export content to file
            Utils.exportToFile(Constants.DEFAULT_OUTPUT_JSON_FILENAME, dataToExport); // Export content to file
        }
    }

    private AdapterView.OnItemClickListener modifyPersonData = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(DatosListActivity.this, LanzaActividad.class);

            // Add extra attributes
            intent.putExtra(Constants.INTENT_REQUEST_CODE_KEY, Constants.LAUNCH_SECOND_ACTIVITY_TO_MODIFY);
            intent.putExtra(Constants.INTENT_ELEMENT_POSITION_TO_MODIFY_KEY, position);
            intent.putExtra(Constants.INTENT_ELEMENT_DATA_TO_MODIFY_KEY, (Serializable) DatosListActivity.this.listViewArrayAdapter.getItem(position));

            startActivityForResult(intent, Constants.LAUNCH_SECOND_ACTIVITY_TO_MODIFY);
        }
    };

    private AdapterView.OnItemLongClickListener deletePersonData = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            UnaPersona personToDelete = DatosListActivity.this.listViewArrayAdapter.getItem(position);

            // Set dialog alert in case of delete
            new AlertDialog.Builder(DatosListActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getString(R.string.main_view_delete_dialog_title_text))
                    .setMessage(getResources().getString(R.string.main_view_delete_dialog_description_text) + ' ' + personToDelete.getName() + ' ' + personToDelete.getSurename() + '?' )
                    .setPositiveButton(getResources().getString(R.string.pc_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatosListActivity.this.listViewArrayAdapter.remove(personToDelete);
                            DatosListActivity.this.userStorage.delete(personToDelete);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.pc_no), null)
                    .show();
            return true;
        }
    };

    public void exit(View view) {
        finish();
    }


}