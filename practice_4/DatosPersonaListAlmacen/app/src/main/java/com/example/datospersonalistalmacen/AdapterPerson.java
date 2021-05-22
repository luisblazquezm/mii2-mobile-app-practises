package com.example.datospersonalistalmacen;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPerson extends ArrayAdapter<UnaPersona> {

    private final ArrayList<UnaPersona> peopleList;
    private SharedPreferences sharedSettings;

    public AdapterPerson(Context context, ArrayList<UnaPersona> peopleList) {
        super(context, 0, peopleList);
        this.peopleList = peopleList;

        // Get settings for all the app
        this.sharedSettings = context.getSharedPreferences(CONSTANTS.SHARED_SETTINGS_KEY, Context.MODE_PRIVATE);
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
        TextView tvDate = (TextView) rowView.findViewById(R.id.listItemDate);

        // Populate the data into the template view using the data object
        tvName.setText(person.getName() + " " + person.getSurename());
        tvAge.setText(person.getAge());
        tvDrivingLicense.setText((person.getHasDrivingLicense() ? R.string.pc_yes : R.string.pc_no));
        tvEnglishLevel.setText(person.getEnglishLevel());
        tvDate.setText(person.parseFromDateToString(person.getDate()));

        // Based on the settings set visibility of the fields
        tvAge.setVisibility(this.sharedSettings.getBoolean(CONSTANTS.AGE_VISUALIZATION_SETTINGS_KEY, true) ? View.VISIBLE : View.GONE);
        tvDrivingLicense.setVisibility(this.sharedSettings.getBoolean(CONSTANTS.DRIVING_LICENSE_VISUALIZATION_SETTINGS_KEY, true) ? View.VISIBLE : View.GONE);
        tvEnglishLevel.setVisibility(this.sharedSettings.getBoolean(CONSTANTS.REGISTRY_DATE_VISUALIZATION_SETTINGS_KEY, true) ? View.VISIBLE : View.GONE);
        //tvAge.setVisibility(this.sharedSettings.getBoolean(CONSTANTS.PHONE_VISUALIZATION_SETTINGS_KEY, true) ? View.VISIBLE : View.GONE);
        tvDate.setVisibility(this.sharedSettings.getBoolean(CONSTANTS.ENGLISH_LEVEL_VISUALIZATION_SETTINGS_KEY, true) ? View.VISIBLE : View.GONE);

        // Return the completed view to render on screen
        return rowView;
    }
}
