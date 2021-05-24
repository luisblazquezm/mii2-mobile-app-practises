package com.example.datospersonalistalmacen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.datospersonalistalmacen.utils.Constants;

public class SettingsActivity extends Activity {

    // UI ELEMENTS
    private TextView urlEditText = null;
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
        this.urlEditText = (TextView) findViewById(R.id.urlEditText);
        this.formatRadioGroup = (RadioGroup) findViewById(R.id.dataFormatRadioGroup);
        this.storageTypeRadioGroup = (RadioGroup) findViewById(R.id.storageTypeRadioGroup);
        this.communicationRadioGroup = (RadioGroup) findViewById(R.id.communicationTypeRadioGroup);
        this.ageVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizeAgeCheckBox);
        this.drivingLicenseVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizeDrivingLicenseCheckBox);
        this.registryDateVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizeRegistryDateCheckBox);
        this.phoneVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizePhoneCheckBox);
        this.englishLevelVisualizationCheckbox = (CheckBox) findViewById(R.id.visualizeEnglishLevelCheckBox);

        // Get settings for all the app
        this.sharedSettings = SettingsActivity.this.getSharedPreferences(Constants.SHARED_SETTINGS_KEY, Context.MODE_PRIVATE);

        // Set settings
        this.setInitSettingsValues();
    }

    private void setInitSettingsValues() {
        // URL
        this.urlEditText.setText(this.sharedSettings.getString(Constants.URL_SETTINGS_KEY, ""));

        // Radio groups
        this.formatRadioGroup.check(this.sharedSettings.getInt(Constants.FORMAT_SETTINGS_KEY, R.id.xmlFormatRadioButton));
        this.storageTypeRadioGroup.check(this.sharedSettings.getInt(Constants.STORAGE_TYPE_SETTINGS_KEY, R.id.externalMemoryRadioButton));
        this.communicationRadioGroup.check(this.sharedSettings.getInt(Constants.COMMUNICATION_TYPE_SETTINGS_KEY, R.id.asyncTasksRadioButton));

        // Checkboxes
        this.ageVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(Constants.AGE_VISUALIZATION_SETTINGS_KEY, true));
        this.drivingLicenseVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(Constants.DRIVING_LICENSE_VISUALIZATION_SETTINGS_KEY, true));
        this.registryDateVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(Constants.REGISTRY_DATE_VISUALIZATION_SETTINGS_KEY, true));
        this.phoneVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(Constants.PHONE_VISUALIZATION_SETTINGS_KEY, true));
        this.englishLevelVisualizationCheckbox.setChecked(this.sharedSettings.getBoolean(Constants.ENGLISH_LEVEL_VISUALIZATION_SETTINGS_KEY, true));
    }

    public void saveSettings(View view) {

        // Save and store settings
        SharedPreferences.Editor editor = this.sharedSettings.edit();

        // URL
        editor.putString(Constants.URL_SETTINGS_KEY, this.urlEditText.getText().toString());

        // Radio groups
        editor.putInt(Constants.FORMAT_SETTINGS_KEY, this.formatRadioGroup.getCheckedRadioButtonId());
        editor.putInt(Constants.STORAGE_TYPE_SETTINGS_KEY, this.storageTypeRadioGroup.getCheckedRadioButtonId());
        editor.putInt(Constants.COMMUNICATION_TYPE_SETTINGS_KEY, this.communicationRadioGroup.getCheckedRadioButtonId());

        // Checkboxes
        editor.putBoolean(Constants.AGE_VISUALIZATION_SETTINGS_KEY, this.ageVisualizationCheckbox.isChecked());
        editor.putBoolean(Constants.DRIVING_LICENSE_VISUALIZATION_SETTINGS_KEY, this.drivingLicenseVisualizationCheckbox.isChecked());
        editor.putBoolean(Constants.REGISTRY_DATE_VISUALIZATION_SETTINGS_KEY, this.registryDateVisualizationCheckbox.isChecked());
        editor.putBoolean(Constants.PHONE_VISUALIZATION_SETTINGS_KEY, this.phoneVisualizationCheckbox.isChecked());
        editor.putBoolean(Constants.ENGLISH_LEVEL_VISUALIZATION_SETTINGS_KEY, this.englishLevelVisualizationCheckbox.isChecked());

        // Commit and store changes
        editor.commit();

        // Finish
        this.exitSettings(view);
    }

    public void exitSettings(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent); // set canceled flag in result
        finish(); // finish new activity
    }
}