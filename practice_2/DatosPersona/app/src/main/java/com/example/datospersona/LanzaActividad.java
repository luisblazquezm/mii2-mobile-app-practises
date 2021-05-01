package com.example.datospersona;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

public class LanzaActividad extends AppCompatActivity {

    // ATTRIBUTES
    private String name;
    private String surenames;
    private String age;
    private String phone;
    private boolean hasDrivingLicense;
    private int englishLevelID;
    private String date;

    // UI XML ELEMENTS
    private EditText nameEditText;
    private EditText surenamesEditText;
    private EditText ageEditText;
    private EditText phoneEditText;
    private CheckBox drivingLicenseCheckbox;
    private RadioGroup englishLevelRadioGroup;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // recovering the instance state
        if (savedInstanceState != null) {
            this.name = savedInstanceState.getString(CONSTANTS.NAME_STATE_KEY);
            this.surenames = savedInstanceState.getString(CONSTANTS.SURENAMES_STATE_KEY);
            this.age = savedInstanceState.getString(CONSTANTS.AGE_STATE_KEY);
            this.phone = savedInstanceState.getString(CONSTANTS.PHONE_STATE_KEY);
            this.hasDrivingLicense = savedInstanceState.getBoolean(CONSTANTS.DRIVING_LICENSE_STATE_KEY);
            this.englishLevelID = savedInstanceState.getInt(CONSTANTS.ENGLISH_LEVEL_STATE_KEY);
            this.date = savedInstanceState.getString(CONSTANTS.DATE_STATE_KEY);
        }

        setContentView(R.layout.activity_lanza_actividad2);

        // Initialize person attributes
        this.date = this.getCurrenDate();

        // Get UI element references
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.surenamesEditText= (EditText) findViewById(R.id.surenamesEditText);
        this.ageEditText = (EditText) findViewById(R.id.ageEditText);
        this.phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        this.drivingLicenseCheckbox = (CheckBox) findViewById(R.id.drivingLicenseCheckBox);
        this.englishLevelRadioGroup = (RadioGroup) findViewById(R.id.englishLevelRadioGroup);
        this.dateTextView = (TextView) findViewById(R.id.dateTextView);

        // Set current date in initialization
        this.dateTextView.setText(this.getCurrenDate());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.nameEditText.setText(savedInstanceState.getString(CONSTANTS.NAME_STATE_KEY));
        this.surenamesEditText.setText(savedInstanceState.getString(CONSTANTS.SURENAMES_STATE_KEY));
        this.ageEditText.setText(savedInstanceState.getString(CONSTANTS.AGE_STATE_KEY));
        this.phoneEditText.setText(savedInstanceState.getString(CONSTANTS.PHONE_STATE_KEY));
        this.drivingLicenseCheckbox.setChecked(savedInstanceState.getBoolean(CONSTANTS.DRIVING_LICENSE_STATE_KEY));
        this.englishLevelRadioGroup.check(savedInstanceState.getInt(CONSTANTS.ENGLISH_LEVEL_STATE_KEY));
        this.dateTextView.setText(savedInstanceState.getString(CONSTANTS.DATE_STATE_KEY));
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CONSTANTS.NAME_STATE_KEY, this.nameEditText.getText().toString());
        outState.putString(CONSTANTS.SURENAMES_STATE_KEY, this.surenamesEditText.getText().toString());
        outState.putString(CONSTANTS.AGE_STATE_KEY, this.ageEditText.getText().toString());
        outState.putString(CONSTANTS.PHONE_STATE_KEY, this.phoneEditText.getText().toString());
        outState.putBoolean(CONSTANTS.DRIVING_LICENSE_STATE_KEY, this.drivingLicenseCheckbox.isChecked());

        int selectedEnglishLevelButtonId = this.englishLevelRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedEnglishLevelRadioButton = (RadioButton) findViewById(selectedEnglishLevelButtonId);
        outState.putString(CONSTANTS.ENGLISH_LEVEL_STATE_KEY, selectedEnglishLevelRadioButton.getText().toString());

        outState.putString(CONSTANTS.DATE_STATE_KEY, this.dateTextView.getText().toString());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    private String getCurrenDate() {
        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return day + "/" + month + "/" + year;
    }

    public void changeDate(View view) {

        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Add event listener to display the datepicker with the current date set by default
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthofyear, int dayofmonth) {
                date = dayofmonth + "/" + monthofyear + "/" + year; // change attribute of a new person
                dateTextView.setText(date); // set in the text the new date
            }
        }, year, month, day);

        // Show and display datepicker when the button is pressed
        dpd.show();
    }

    public void savePerson(View view) {

        // ----------- NAME
        this.name = this.nameEditText.getText().toString();

        // ----------- SURENAMES
        this.surenames = this.surenamesEditText.getText().toString();
        if (this.name.isEmpty() && surenames.isEmpty()) {
            this.name = CONSTANTS.UNKNOWN_DEFAULT_VALUE;
            surenames = "";
        }

        // ----------- AGE
        this.age = this.ageEditText.getText().toString();
        if (age.isEmpty()) {
            this.age = CONSTANTS.UNKNOWN_DEFAULT_VALUE;
        }

        // ----------- PHONE
        this.phone = this.phoneEditText.getText().toString();
        if (this.phone.isEmpty()) {
            this.phone = CONSTANTS.UNKNOWN_DEFAULT_VALUE;
        }

        // ----------- DRIVING LICENSE
        this.hasDrivingLicense = this.drivingLicenseCheckbox.isChecked();

        // ----------- ENGLISH LEVEL
        this.englishLevelID = this.englishLevelRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedEnglishLevelRadioButton = (RadioButton) findViewById(this.englishLevelID);
        String englishLevel = selectedEnglishLevelRadioButton.getText().toString();

        // DATE is set on 'setDate' function or initialization

        // Pass data from activity to main activity with intent
        Intent returnIntent = new Intent(this, MainActivity.class);
        returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the activity stack so that the user cannot go back to the previous activity with the BACK button
        returnIntent.putExtra(CONSTANTS.INTENT_NAME_KEY, this.name);
        returnIntent.putExtra(CONSTANTS.INTENT_SURENAMES_KEY, this.surenames);
        returnIntent.putExtra(CONSTANTS.INTENT_AGE_KEY, this.age);
        returnIntent.putExtra(CONSTANTS.INTENT_PHONE_KEY, this.phone);
        returnIntent.putExtra(CONSTANTS.INTENT_DRIVING_LICENSE_KEY, this.hasDrivingLicense);
        returnIntent.putExtra(CONSTANTS.INTENT_ENGLISH_LEVEL_KEY, englishLevel);
        returnIntent.putExtra(CONSTANTS.INTENT_DATE_KEY, this.date);
        setResult(Activity.RESULT_OK, returnIntent); // set ok flag in result
        finish(); // finish new activity
    }

    public void resetPerson(View view) {

        // Empty name field
        this.nameEditText.getText().clear();
        // Empty surenames field
        this.surenamesEditText.getText().clear();
        // Empty age field
        this.ageEditText.getText().clear();
        // Empty phone field
        this.phoneEditText.getText().clear();
        // Uncheck driving license checkbox
        if(this.drivingLicenseCheckbox.isChecked()){
            this.drivingLicenseCheckbox.toggle();
        }
        // Set low level as default
        this.englishLevelRadioGroup.clearCheck();
        this.englishLevelRadioGroup.check(R.id.lowLevelRadioButton);
        // Set current date
        this.dateTextView.setText(this.getCurrenDate());
    }

    public void cancel(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent); // set canceled flag in result
        finish(); // finish new activity
    }
}