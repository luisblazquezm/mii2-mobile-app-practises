package com.example.datospersonalistcarga;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPerson extends ArrayAdapter<UnaPersona> {

    private final ArrayList<UnaPersona> peopleList;

    public AdapterPerson(Context context, ArrayList<UnaPersona> peopleList) {
        super(context, 0, peopleList);
        this.peopleList = peopleList;
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
        TextView tvDrivinLicense = (TextView) rowView.findViewById(R.id.listItemDrivingLicense);
        TextView tvEnglishLevel = (TextView) rowView.findViewById(R.id.listItemEnglishLevel);
        TextView tvDate = (TextView) rowView.findViewById(R.id.listItemDate);

        // Populate the data into the template view using the data object
        tvName.setText(person.getName() + " " + person.getSurename());
        tvAge.setText(person.getAge());
        tvDrivinLicense.setText((person.getHasDrivingLicense() ? R.string.pc_yes : R.string.pc_no));
        tvEnglishLevel.setText(person.getEnglishLevel());
        tvDate.setText(person.parseFromDateToString(person.getDate()));

        // Return the completed view to render on screen
        return rowView;
    }
}
