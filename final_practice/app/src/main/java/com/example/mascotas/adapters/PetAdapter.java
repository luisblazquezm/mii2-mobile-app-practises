package com.example.mascotas.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mascotas.R;

import com.example.mascotas.interfaces.Constants;
import com.example.mascotas.models.Pet;

import java.util.ArrayList;

public class PetAdapter extends ArrayAdapter<Pet> {

    private ArrayList<Pet> petList;

    public PetAdapter(Context context, ArrayList<Pet> petList) {
        super(context, 0, petList);
        this.petList = petList;
    }

    @Override
    public Pet getItem(int position){
        return this.petList.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return this.petList.size();
    }

    private int getPetImageFromType(String type) {
        // Check if null
        if (null == type)
            return R.drawable.other;

        switch(type){
            case Constants.CAT_VALUE:
                return  R.drawable.cat;
            case Constants.BIRD_VALUE:
                return  R.drawable.bird;
            case Constants.DOG_VALUE:
                return  R.drawable.dog;
            default:
                return  R.drawable.other;
        }
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        // Get the data item for this position
        Pet pet = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (rowView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_pet_layout, parent,
                    false);
        }

        // Get UI elements reference from layout
        TextView petNameLV = (TextView) rowView.findViewById(R.id.listViewPetName);
        TextView petIsVaccinatedLV = (TextView) rowView.findViewById(R.id.listViewPetVaccinatedState);
        TextView petDateLV = (TextView) rowView.findViewById(R.id.listViewPetVaccineDate);
        TextView petOwnerLV = (TextView) rowView.findViewById(R.id.listViewPetOwner);
        ImageView petImageLV = (ImageView) rowView.findViewById(R.id.listViewPetImage);

        // Populate the data into the template view using the data object
        petNameLV.setText(pet.getName());
        if (pet.isVaccinated()){
            petIsVaccinatedLV.setText(R.string.vaccinated_label_text);
            petIsVaccinatedLV.setTextColor(Color.GREEN);
        } else {
            petIsVaccinatedLV.setText(R.string.not_vaccinated_label_text);
            petIsVaccinatedLV.setTextColor(Color.RED);
        }
        petOwnerLV.setText(pet.getOwner());
        petDateLV.setText(pet.getStringDate());
        petImageLV.setImageResource(this.getPetImageFromType(pet.getType()));

        // Return the completed view to render on screen
        return rowView;
    }

    public void updateResults(ArrayList<Pet> updatedList) {
        this.petList = updatedList; // Set new updated list
        notifyDataSetChanged(); // Refresh data
    }
}
