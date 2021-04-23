package com.example.datospersonalist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<UnaPersona> peopleList = new ArrayList<UnaPersona>();
    private ListView listView = null;
    private AdapterPerson listViewArrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialice list
        this.fillListInInitialization();

        // Capture the layout's TextView and set the string as its text
        this.listView = (ListView) findViewById(R.id.peopleListView);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and the
        // array as a third parameter.
        this.listViewArrayAdapter = new AdapterPerson(this, this.peopleList);

        this.listView.setAdapter(this.listViewArrayAdapter); // set adapter
        this.listView.setOnItemClickListener(modifyPersonData); // set event when click on element
    }

    private void fillListInInitialization() {
        this.peopleList.add(new UnaPersona("Luis", "Blázquez Miñambres", "23",
                "34343434", true,
                "ALTO", "23/04/2021"));
        this.peopleList.add(new UnaPersona("Manuel", "Pérez Pérez", "53",
                "45454545", true,
                "MEDIO", "12/04/2021"));
        this.peopleList.add(new UnaPersona("Marcos", "Rodriguez Pérez", "45",
                "999999999", false,
                "BAJO", "13/04/2021"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check the type of operation requested by request code sent in startActivityForResult
        if (requestCode == CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_ADD) {
            if(resultCode == Activity.RESULT_OK) { // new person added
                // Get the Intent that started this activity and extract the string
                if (data != null) {
                    // Get extra attributes
                    UnaPersona newPerson = (UnaPersona) data.getSerializableExtra(CONSTANTS.INTENT_ELEMENT_NEW_PERSON_KEY);

                    // Add new person
                    peopleList.add(newPerson);

                    // Notify adapter about the modification to update the data in list
                    this.listViewArrayAdapter.notifyDataSetChanged();
                }
            } else if (resultCode != Activity.RESULT_CANCELED) { // canceled
                // Nothing
            }
        } else if (requestCode == CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_MODIFY) {
            if(resultCode == Activity.RESULT_OK) { // new person added
                // Get the Intent that started this activity and extract the string
                if (data != null) {
                    // Get extra attributes
                    int position = data.getIntExtra(CONSTANTS.INTENT_ELEMENT_POSITION_TO_MODIFY_KEY, 0);
                    UnaPersona personModified = (UnaPersona) data.getSerializableExtra(CONSTANTS.INTENT_ELEMENT_NEW_PERSON_KEY);

                    // Set new data
                    peopleList.set(position, personModified);

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
        intent.putExtra(CONSTANTS.INTENT_REQUEST_CODE_KEY, CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_ADD); // add request code to identify operation in activity
        startActivityForResult(intent, CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_ADD);
    }

    private AdapterView.OnItemClickListener modifyPersonData = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, LanzaActividad.class);

            // Add extra attributes
            intent.putExtra(CONSTANTS.INTENT_REQUEST_CODE_KEY, CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_MODIFY);
            intent.putExtra(CONSTANTS.INTENT_ELEMENT_POSITION_TO_MODIFY_KEY, position);
            intent.putExtra(CONSTANTS.INTENT_ELEMENT_DATA_TO_MODIFY_KEY, (Serializable) MainActivity.this.listViewArrayAdapter.getItem(position));

            startActivityForResult(intent, CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_MODIFY);
        }
    };

    public void exit(View view) {
        finish();
    }
}