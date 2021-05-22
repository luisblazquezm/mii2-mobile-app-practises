package com.example.datospersonalistalmacen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    // UI ELEMENTS
    private TextView urlTextView = null;
    private RadioGroup formatRadioGroup = null;
    private RadioGroup storageTypeRadioGroup = null;
    private RadioGroup communicationRadioGroup = null;
    private CheckBox ageVisualizationCheckbox = null;
    private CheckBox drivingLicenseVisualizationCheckbox = null;
    private CheckBox registryDateVisualizationCheckbox = null;
    private CheckBox phoneVisualizationCheckbox = null;
    private CheckBox englishLevelVisualizationCheckbox = null;

    // OTHER
    private SharedPreferences sharedSettings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get UI element references
        this.urlTextView = (TextView) findViewById(R.id.urlTextView);
        this.formatRadioGroup = (RadioGroup) findViewById(R.id.dataFormatRadioGroup);
        this.storageTypeRadioGroup = (RadioGroup) findViewById(R.id.storageTypeRadioGroup);
        this.communicationRadioGroup = (RadioGroup) findViewById(R.id.communicationTypeRadioGroup);
        this.ageVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizeAgeCheckBox);
        this.drivingLicenseVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizeDrivingLicenseCheckBox);
        this.registryDateVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizeRegistryDateCheckBox);
        this.phoneVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizePhoneCheckBox);
        this.englishLevelVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizeEnglishLevelCheckBox);

        // Get settings for all the app
        this.sharedSettings = SettingsActivity.this.getSharedPreferences(CONSTANTS.SHARED_SETTINGS_KEY, Context.MODE_PRIVATE);

        // Set settings
        this.setInitSettingsValues();
    }

    private void setInitSettingsValues() {
        // URL
        this.urlTextView.setText(this.sharedSettings.getString(CONSTANTS.URL_SETTINGS_KEY, ""));

        // Radio groups
        this.formatRadioGroup.check(this.sharedSettings.getInt(CONSTANTS.FORMAT_SETTINGS_KEY, R.id.xmlFormatRadioButton));
        this.storageTypeRadioGroup.check(this.sharedSettings.getInt(CONSTANTS.STORAGE_TYPE_SETTINGS_KEY, R.id.internalMemoryRadioButton));
        this.communicationRadioGroup.check(this.sharedSettings.getInt(CONSTANTS.COMMUNICATION_TYPE_SETTINGS_KEY, R.id.asyncTasksRadioButton));

        // Checkboxes
        this.ageVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(CONSTANTS.AGE_VISUALIZATION_SETTINGS_KEY, true));
        this.drivingLicenseVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(CONSTANTS.DRIVING_LICENSE_VISUALIZATION_SETTINGS_KEY, true));
        this.registryDateVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(CONSTANTS.REGISTRY_DATE_VISUALIZATION_SETTINGS_KEY, true));
        this.phoneVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(CONSTANTS.PHONE_VISUALIZATION_SETTINGS_KEY, true));
        this.englishLevelVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(CONSTANTS.ENGLISH_LEVEL_VISUALIZATION_SETTINGS_KEY, true));
    }

    public void saveSettings(View view) {

        // Save and store settings
        SharedPreferences.Editor editor = this.sharedSettings.edit();

        // URL
        editor.putString(CONSTANTS.URL_SETTINGS_KEY, this.urlTextView.getText().toString());

        // Radio groups
        editor.putInt(CONSTANTS.FORMAT_SETTINGS_KEY, this.formatRadioGroup.getCheckedRadioButtonId());
        editor.putInt(CONSTANTS.STORAGE_TYPE_SETTINGS_KEY, this.storageTypeRadioGroup.getCheckedRadioButtonId());
        editor.putInt(CONSTANTS.COMMUNICATION_TYPE_SETTINGS_KEY, this.communicationRadioGroup.getCheckedRadioButtonId());

        // Checkboxes
        editor.putBoolean(CONSTANTS.AGE_VISUALIZATION_SETTINGS_KEY, this.ageVisualizationCheckbox.isChecked());
        editor.putBoolean(CONSTANTS.DRIVING_LICENSE_VISUALIZATION_SETTINGS_KEY, this.drivingLicenseVisualizationCheckbox.isChecked());
        editor.putBoolean(CONSTANTS.REGISTRY_DATE_VISUALIZATION_SETTINGS_KEY, this.registryDateVisualizationCheckbox.isChecked());
        editor.putBoolean(CONSTANTS.PHONE_VISUALIZATION_SETTINGS_KEY, this.phoneVisualizationCheckbox.isChecked());
        editor.putBoolean(CONSTANTS.ENGLISH_LEVEL_VISUALIZATION_SETTINGS_KEY, this.englishLevelVisualizationCheckbox.isChecked());

        // Commit and store changes
        editor.commit();
    }

    public void exitSettings(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent); // set canceled flag in result
        finish(); // finish new activity
    }
}