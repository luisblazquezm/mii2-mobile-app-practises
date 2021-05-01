package com.example.datospersona;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int LAUNCH_SECOND_ACTIVITY = 1;

    private static String peopleList = "";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // recovering the instance state
        if (savedInstanceState != null) {
            this.peopleList = savedInstanceState.getString(CONSTANTS.PEOPLE_LIST_STATE_KEY);
        }

        setContentView(R.layout.activity_main);

        // Capture the layout's TextView and set the string as its text
        this.textView = (TextView) findViewById(R.id.peopleListTextView);
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.textView.setText(savedInstanceState.getString(CONSTANTS.PEOPLE_LIST_STATE_KEY));
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CONSTANTS.PEOPLE_LIST_STATE_KEY, this.textView.getText().toString());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == this.LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK) { // new person added
                // Get the Intent that started this activity and extract the string
                if (data != null) {
                    String name = data.getStringExtra(CONSTANTS.INTENT_NAME_KEY);
                    String surenames = data.getStringExtra(CONSTANTS.INTENT_SURENAMES_KEY);
                    String age = data.getStringExtra(CONSTANTS.INTENT_AGE_KEY);
                    String phone = data.getStringExtra(CONSTANTS.INTENT_PHONE_KEY);
                    boolean hasDrivingLicense = data.getBooleanExtra(CONSTANTS.INTENT_DRIVING_LICENSE_KEY, false);
                    String englishLevel = data.getStringExtra(CONSTANTS.INTENT_ENGLISH_LEVEL_KEY);
                    String date = data.getStringExtra(CONSTANTS.INTENT_DATE_KEY);
                    String newPerson = "Nombre: " + name + " " + surenames + "\n" +
                            "Edad: " + age + " " +
                            "PC: " + (hasDrivingLicense ? "SI" : "NO") + " " +
                            "Inglés: " + englishLevel + " " +
                            "ALTO INGRESO: " + date + "\n";
                    this.peopleList += newPerson;
                }
            } else if (resultCode != Activity.RESULT_CANCELED) { // canceled
                // Nothing
            }

            // Set people list
            textView.setText(this.peopleList);
        }
    }

    public void addPersonData(View view) {
        Intent intent = new Intent(this, LanzaActividad.class);
        startActivityForResult(intent, this.LAUNCH_SECOND_ACTIVITY);
    }

    public void exit(View view) {
        finish();
    }
}