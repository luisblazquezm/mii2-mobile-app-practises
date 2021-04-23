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
        String name = this.nameEditText.getText().toString();

        // ----------- SURENAMES
        String surenames = this.surenamesEditText.getText().toString();
        if (name.isEmpty() && surenames.isEmpty()) {
            name = "desconocido";
            surenames = "";
        }

        // ----------- AGE
        String age = this.ageEditText.getText().toString();
        if (age.isEmpty()) {
            age = "desconocido";
        }

        // ----------- PHONE
        String phone = this.phoneEditText.getText().toString();
        if (phone.isEmpty()) {
            phone = "desconocido";
        }

        // ----------- DRIVING LICENSE
        boolean hasDrivingLicense = this.drivingLicenseCheckbox.isChecked();

        // ----------- ENGLISH LEVEL
        int selectedEnglishLevelButtonId = this.englishLevelRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedEnglishLevelRadioButton = (RadioButton) findViewById(selectedEnglishLevelButtonId);
        String englishLevel = selectedEnglishLevelRadioButton.getText().toString();

        // DATE is set on 'setDate' function or initialization

        // Pass data from activity to main activity with intent
        Intent returnIntent = new Intent(this, MainActivity.class);
        returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the activity stack so that the user cannot go back to the previous activity with the BACK button
        returnIntent.putExtra(CONSTANTS.INTENT_NAME_KEY, name);
        returnIntent.putExtra(CONSTANTS.INTENT_SURENAMES_KEY, surenames);
        returnIntent.putExtra(CONSTANTS.INTENT_AGE_KEY, age);
        returnIntent.putExtra(CONSTANTS.INTENT_PHONE_KEY, phone);
        returnIntent.putExtra(CONSTANTS.INTENT_DRIVING_LICENSE_KEY, hasDrivingLicense);
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