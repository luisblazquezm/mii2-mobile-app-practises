package com.example.datospersonalist;

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
    private int requestCode;

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
        setContentView(R.layout.activity_lanza_actividad);

        // Get UI element references
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.surenamesEditText= (EditText) findViewById(R.id.surenamesEditText);
        this.ageEditText = (EditText) findViewById(R.id.ageEditText);
        this.phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        this.drivingLicenseCheckbox = (CheckBox) findViewById(R.id.drivingLicenseCheckBox);
        this.englishLevelRadioGroup = (RadioGroup) findViewById(R.id.englishLevelRadioGroup);
        this.dateTextView = (TextView) findViewById(R.id.dateTextView);

        // Get data to modify
        Bundle intentExtraParameters = getIntent().getExtras();
        if (intentExtraParameters != null) {
            this.requestCode = intentExtraParameters.getInt(CONSTANTS.INTENT_REQUEST_CODE_KEY);

            if (CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_MODIFY == this.requestCode) { // To modify
                UnaPersona personToModify = (UnaPersona) getIntent().getSerializableExtra(CONSTANTS.INTENT_ELEMENT_DATA_TO_MODIFY_KEY);
                this.setValuesIntoFields(personToModify); // set values to modify
            } else if (CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_ADD == this.requestCode) { // To add new data
                this.date = this.getCurrenDate(); // initialize person attributes
                this.dateTextView.setText(this.date); // set current date in initialization
            }
        }
    }

    private void setValuesIntoFields(UnaPersona p) {
        // Name field
        this.nameEditText.setText(p.getName());
        // Surenames field
        this.surenamesEditText.setText(p.getSurename());
        // Age field
        this.ageEditText.setText(p.getAge());
        // Phone field
        this.phoneEditText.setText(p.getPhone());
        // Driving license checkbox
        this.drivingLicenseCheckbox.setChecked(p.getHasDrivingLicense());
        // English level radiogroup
        int count = this.englishLevelRadioGroup.getChildCount();
        for (int i=0;i<count;i++) {
            RadioButton b = (RadioButton) this.englishLevelRadioGroup.getChildAt(i);
            if (p.getEnglishLevel().equals(b.getText())) {
                this.englishLevelRadioGroup.check(b.getId());
            }
        }
        // Set current date
        this.dateTextView.setText(p.getDate());
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

        UnaPersona newPerson = new UnaPersona();

        // ----------- NAME
        String name = this.nameEditText.getText().toString();

        // ----------- SURENAMES
        String surenames = this.surenamesEditText.getText().toString();
        if (name.isEmpty() && surenames.isEmpty()) {
            name = "desconocido";
            surenames = "";
        }
        newPerson.setName(name);
        newPerson.setSurename(surenames);

        // ----------- AGE
        String age = this.ageEditText.getText().toString();
        if (age.isEmpty()) {
            age = "desconocido";
        }
        newPerson.setAge(age);

        // ----------- PHONE
        String phone = this.phoneEditText.getText().toString();
        if (phone.isEmpty()) {
            phone = "desconocido";
        }
        newPerson.setPhone(phone);

        // ----------- DRIVING LICENSE
        boolean hasDrivingLicense = this.drivingLicenseCheckbox.isChecked();
        newPerson.setHasDrivingLicense(hasDrivingLicense);

        // ----------- ENGLISH LEVEL
        int selectedEnglishLevelButtonId = this.englishLevelRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedEnglishLevelRadioButton = (RadioButton) findViewById(selectedEnglishLevelButtonId);
        String englishLevel = selectedEnglishLevelRadioButton.getText().toString();
        newPerson.setEnglishLevel(englishLevel);

        // ----------- ENGLISH LEVEL
        newPerson.setDate(this.date);

        // Pass data from activity to main activity with intent
        Intent returnIntent = new Intent(this, MainActivity.class);
        returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the activity stack so that the user cannot go back to the previous activity with the BACK button
        returnIntent.putExtra(CONSTANTS.INTENT_ELEMENT_NEW_PERSON_KEY, newPerson);

        // Send position of element if is to modify
        if (getIntent().hasExtra(CONSTANTS.INTENT_ELEMENT_POSITION_TO_MODIFY_KEY) &&
                CONSTANTS.LAUNCH_SECOND_ACTIVITY_TO_MODIFY == this.requestCode){
            int elementPosition = getIntent().getIntExtra(CONSTANTS.INTENT_ELEMENT_POSITION_TO_MODIFY_KEY, 0);
            returnIntent.putExtra(CONSTANTS.INTENT_ELEMENT_POSITION_TO_MODIFY_KEY, elementPosition);
        }

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