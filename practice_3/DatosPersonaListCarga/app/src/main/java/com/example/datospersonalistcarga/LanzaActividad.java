package com.example.datospersonalistcarga;

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

public class LanzaActividad extends Activity {

    // ATTRIBUTES
    private UnaPersona newPerson;
    private String dateString;
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

        // recovering the instance state
        if (savedInstanceState != null) {
            this.newPerson.setName(savedInstanceState.getString(CONSTANTS.NAME_STATE_KEY));
            this.newPerson.setSurename(savedInstanceState.getString(CONSTANTS.SURENAMES_STATE_KEY));
            this.newPerson.setAge(savedInstanceState.getString(CONSTANTS.AGE_STATE_KEY));
            this.newPerson.setPhone(savedInstanceState.getString(CONSTANTS.PHONE_STATE_KEY));
            this.newPerson.setHasDrivingLicense(savedInstanceState.getBoolean(CONSTANTS.DRIVING_LICENSE_STATE_KEY));

            RadioButton rb = (RadioButton) findViewById(savedInstanceState.getInt(CONSTANTS.ENGLISH_LEVEL_STATE_KEY));
            String radioText = rb.getText().toString();
            this.newPerson.setEnglishLevel(radioText);

            this.newPerson.setDate(this.newPerson.parseFromStringToDate(savedInstanceState.getString(CONSTANTS.DATE_STATE_KEY)));
        }

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
                this.dateString = this.getCurrenDate(); // initialize person attributes
                this.dateTextView.setText(this.dateString); // set current date in initialization
            }
        }
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

    private void setValuesIntoFields(UnaPersona p) {
        // Name field
        this.nameEditText.setText(p.getName());
        // Surenames field
        this.surenamesEditText.setText(p.getSurename());
        // Age field (check values because its a number field and not text)
        String age = p.getAge();
        if (age.equals(CONSTANTS.UNKNOWN_DEFAULT_VALUE)){
            this.ageEditText.setText("");
        } else {
            this.ageEditText.setText(p.getAge());
        }
        // Phone field (check values because its a number field and not text)
        String phone = p.getPhone();
        if (phone.equals(CONSTANTS.UNKNOWN_DEFAULT_VALUE)){
            this.phoneEditText.setText("");
        } else {
            this.phoneEditText.setText(p.getPhone());
        }
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
        this.dateTextView.setText(p.getStringDate());
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
                dateString = dayofmonth + "/" + monthofyear + "/" + year; // change attribute of a new person
                dateTextView.setText(dateString); // set in the text the new date
            }
        }, year, month, day);

        // Show and display datepicker when the button is pressed
        dpd.show();
    }

    public void savePerson(View view) {

        this.newPerson = new UnaPersona();

        // ----------- NAME
        String name = this.nameEditText.getText().toString();

        // ----------- SURENAMES
        String surenames = this.surenamesEditText.getText().toString();
        if (name.isEmpty() && surenames.isEmpty()) {
            name = CONSTANTS.UNKNOWN_DEFAULT_VALUE;
            surenames = "";
        }
        this.newPerson.setName(name);
        this.newPerson.setSurename(surenames);

        // ----------- AGE
        String age = this.ageEditText.getText().toString();
        if (age.isEmpty()) {
            age = CONSTANTS.UNKNOWN_DEFAULT_VALUE;
        }
        this.newPerson.setAge(age);

        // ----------- PHONE
        String phone = this.phoneEditText.getText().toString();
        if (phone.isEmpty()) {
            phone = CONSTANTS.UNKNOWN_DEFAULT_VALUE;
        }
        this.newPerson.setPhone(phone);

        // ----------- DRIVING LICENSE
        boolean hasDrivingLicense = this.drivingLicenseCheckbox.isChecked();
        this.newPerson.setHasDrivingLicense(hasDrivingLicense);

        // ----------- ENGLISH LEVEL
        int selectedEnglishLevelButtonId = this.englishLevelRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedEnglishLevelRadioButton = (RadioButton) findViewById(selectedEnglishLevelButtonId);
        String englishLevel = selectedEnglishLevelRadioButton.getText().toString();
        this.newPerson.setEnglishLevel(englishLevel);

        // ----------- DATE LEVEL
        this.newPerson.setDate(this.newPerson.parseFromStringToDate(this.dateString));

        // Pass data from activity to main activity with intent
        Intent returnIntent = new Intent(this, MainActivity.class);
        returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the activity stack so that the user cannot go back to the previous activity with the BACK button
        returnIntent.putExtra(CONSTANTS.INTENT_ELEMENT_NEW_PERSON_KEY, this.newPerson);

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