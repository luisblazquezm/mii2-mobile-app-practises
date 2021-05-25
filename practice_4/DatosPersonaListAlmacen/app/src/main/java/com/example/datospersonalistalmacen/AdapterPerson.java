package com.example.datospersonalistalmacen;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.datospersonalistalmacen.constants.Constants;
import com.example.datospersonalistalmacen.constants.SettingsConstants;

import java.util.ArrayList;

public class AdapterPerson extends ArrayAdapter<UnaPersona> {

    private ArrayList<UnaPersona> peopleList;
    private SharedPreferences sharedSettings;

    public AdapterPerson(Context context, ArrayList<UnaPersona> peopleList) {
        super(context, 0, peopleList);
        this.peopleList = peopleList;

        // Get settings for all the app
        this.sharedSettings = context.getSharedPreferences(SettingsConstants.SHARED_SETTINGS_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public UnaPersona getItem(int position){
        return this.peopleList.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return this.peopleList.size();
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        // Get the data item for this position
        UnaPersona person = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (rowView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_personalized_layout, parent,
                    false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) rowView.findViewById(R.id.listItemName);
        TextView tvAge = (TextView) rowView.findViewById(R.id.listItemAge);
        TextView tvDrivingLicense = (TextView) rowView.findViewById(R.id.listItemDrivingLicense);
        TextView tvEnglishLevel = (TextView) rowView.findViewById(R.id.listItemEnglishLevel);
        TextView tvPhone = (TextView) rowView.findViewById(R.id.listItemPhone);
        TextView tvDate = (TextView) rowView.findViewById(R.id.listItemDate);

        // Get label UI elements
        TextView ageLabel = (TextView) rowView.findViewById(R.id.labelItemAge);
        TextView drivingLicenseLabel = (TextView) rowView.findViewById(R.id.labelItemDrivingLicense);
        TextView englishLevelLabel = (TextView) rowView.findViewById(R.id.labelItemEnglishLevel);
        TextView phoneLabel = (TextView) rowView.findViewById(R.id.labelItemPhone);
        TextView registryDateLabel = (TextView) rowView.findViewById(R.id.labelItemRegistryDate);

        // Populate the data into the template view using the data object
        tvName.setText(person.getName() + " " + person.getSurename());
        tvAge.setText(person.getAge());
        tvDrivingLicense.setText((person.getHasDrivingLicense() ? R.string.pc_yes : R.string.pc_no));
        tvEnglishLevel.setText(person.getEnglishLevel());
        tvPhone.setText(person.getPhone());
        tvDate.setText(person.parseFromDateToString(person.getDate()));

        // Based on the settings set visibility of the fields
        /* AGE */
        boolean isSettingsAgeChecked = this.sharedSettings.getBoolean(SettingsConstants.AGE_VISUALIZATION_SETTINGS_KEY, true);
        tvAge.setVisibility(isSettingsAgeChecked ? View.VISIBLE : View.GONE);
        ageLabel.setVisibility(isSettingsAgeChecked ? View.VISIBLE : View.GONE);

        /* DRIVING LICENSE */
        boolean isSettingsDrivingLicenseChecked = this.sharedSettings.getBoolean(SettingsConstants.DRIVING_LICENSE_VISUALIZATION_SETTINGS_KEY, true);
        tvDrivingLicense.setVisibility(isSettingsDrivingLicenseChecked ? View.VISIBLE : View.GONE);
        drivingLicenseLabel.setVisibility(isSettingsDrivingLicenseChecked ? View.VISIBLE : View.GONE);

        /* ENGLISH LEVEL */
        boolean isSettingsEnglishLevelChecked = this.sharedSettings.getBoolean(SettingsConstants.ENGLISH_LEVEL_VISUALIZATION_SETTINGS_KEY, true);
        tvEnglishLevel.setVisibility(isSettingsEnglishLevelChecked ? View.VISIBLE : View.GONE);
        englishLevelLabel.setVisibility(isSettingsEnglishLevelChecked ? View.VISIBLE : View.GONE);

        /* PHONE */
        boolean isSettingsPhoneChecked = this.sharedSettings.getBoolean(SettingsConstants.PHONE_VISUALIZATION_SETTINGS_KEY, true);
        tvPhone.setVisibility(isSettingsPhoneChecked ? View.VISIBLE : View.GONE);
        phoneLabel.setVisibility(isSettingsPhoneChecked ? View.VISIBLE : View.GONE);

        /* DATE */
        boolean isSettingsDateChecked = this.sharedSettings.getBoolean(SettingsConstants.REGISTRY_DATE_VISUALIZATION_SETTINGS_KEY, true);
        tvDate.setVisibility(isSettingsDateChecked ? View.VISIBLE : View.GONE);
        registryDateLabel.setVisibility(isSettingsDateChecked ? View.VISIBLE : View.GONE);

        // Return the completed view to render on screen
        return rowView;
    }

    public void updateResults(ArrayList<UnaPersona> updatedList) {
        this.peopleList = updatedList; // Set new updated list
        notifyDataSetChanged(); // Refresh data
    }
}
