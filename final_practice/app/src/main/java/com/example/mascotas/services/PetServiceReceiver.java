package com.example.mascotas.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mascotas.activities.PetListActivity;
import com.example.mascotas.interfaces.ServiceConstants;
import com.example.mascotas.models.PetModel;

public class PetServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Check if notification is cancelled or accepted
        boolean isCancelled = intent.getExtras().getBoolean(ServiceConstants.NOTIFICATION_STATE_KEY);

        // Update the list of pending pets and number of events if notification is not cancelled
        // Clear the pending list if cancelled.
        if(!isCancelled) {
            Log.d("d", "Process pending pets");
            PetModel.retrieveInstance().processPendingPet();
        }else{
            Log.d("d", "Clear pending list");
            PetModel.retrieveInstance().clearPendingPets();
        }

        // Update list view in activity for pets
        PetListActivity.updateListViewNotification();
    }

    public PetServiceReceiver(){
        super();
    }
}
