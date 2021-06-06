package com.example.mascotas.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mascotas.R;
import com.example.mascotas.interfaces.Constants;
import com.example.mascotas.interfaces.IOConstants;
import com.example.mascotas.interfaces.ServiceConstants;
import com.example.mascotas.models.Pet;
import com.example.mascotas.models.PetModel;
import com.example.mascotas.models.PetUrlLoader;

import java.util.ArrayList;

public class PetService extends Service {
    // CONSTANTS
    private final static String SENTINEL_URL = Constants.SENTINEL_JSON_PET_URL;
    public static final String ServiceIntent = "custom.MY_SERVICE";

    // VARIABLES
    private int currentNotificationOrder; // Order of notifications
    private NotificationManager manager; // Notification handler
    private PetUrlLoader petLoader;
    private boolean serviceRunning = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize order for notification input
        this.currentNotificationOrder = 0;

        // Initalize pet model
        this.petLoader = new PetUrlLoader();

        // Get and build notification channel
        this.manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String notificationName = getString(R.string.notification_title); /* Name of notification*/
        String notificationDesc = getString(R.string.notification_description); /* Description of notification*/

        // Create channel
        NotificationChannel channel = new NotificationChannel(ServiceConstants.NOTIFICATION_CHANNEL_ID,
                notificationName, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(notificationDesc);
        channel.enableLights(true);
        channel.enableVibration(true);

        // Commit changes to channel
        this.manager.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.serviceRunning = true;

        // Run thread on the background to allow network requests
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                PetService.this.waitForRequests();
            }
        }).start();

        return START_STICKY; // START_STICKY flag automatically to restart after it's been killed
    }

    @Override
    public void onDestroy() {
        this.serviceRunning = false;
        this.stopSelf();
        super.onDestroy();
    }


    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void waitForRequests(){
        while(this.serviceRunning) {
            // Get pet from request
            Pet p = PetService.this.checkForNewPets(this.SENTINEL_URL);
            p.setOrder(++this.currentNotificationOrder);

            // Notify the user that a new has pet has been added to pending
            if(!PetModel.retrieveInstance().getPendingPetsList().contains(p) &&
                    !PetModel.retrieveInstance().getPetList().contains(p)){
                Log.d("d", "New pet added to pending");
                PetModel.retrieveInstance().insertPending(p);
                PetService.this.notifyNewPets();
            } else {
                Log.d("d", "No new pet added");
            }

            // Waiting time for next request
            try {
                Thread.sleep(ServiceConstants.PET_REQUEST_WAITING_TIME);
            } catch (InterruptedException e) {
            }
        }
    }

    public Pet checkForNewPets(String url){
        // Load new pets from url and get first
        Pet p = null;
        try {
            p = this.petLoader.loadFromURL(url, IOConstants.JSON_TYPE_FILE).get(0);
        }catch (Exception e){
            Log.d("d", "checkForNewPets: An exception ocurred");
        }
        Log.d("d", "New pet: " + p.getName());
        return p;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notifyNewPets(){
        // Accept notification activity
        Intent successIntent = new Intent(PetService.this, PetServiceReceiver.class);
        successIntent.putExtra(ServiceConstants.NOTIFICATION_STATE_KEY, false);
        successIntent.putParcelableArrayListExtra(ServiceConstants.NEW_NOTIFICATION_KEY,
                (ArrayList<? extends Parcelable>) PetModel.retrieveInstance().getPendingPetsList());
        PendingIntent successPendingIntent = PendingIntent.getBroadcast (getApplicationContext(),
                0, successIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancel notification intent
        Intent cancelIntent = new Intent(PetService.this, PetServiceReceiver.class);
        cancelIntent.putExtra(ServiceConstants.NOTIFICATION_STATE_KEY, true);
        cancelIntent.putParcelableArrayListExtra(ServiceConstants.NEW_NOTIFICATION_KEY,
                (ArrayList<? extends Parcelable>) PetModel.retrieveInstance().getPendingPetsList());
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast (getApplicationContext(),
                1, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get layout for notification appliance with the number of notifications given
        RemoteViews contentView = new RemoteViews("com.example.mascotas",
                R.layout.notification_layout);
        contentView.setTextViewText(R.id.numNotificationsReceivedTextView,
                Integer.valueOf(PetModel.retrieveInstance().getPendingPetsList().size()).toString());

        // Create and display notification in the main screen
        Notification.Builder builder = new Notification.Builder(this, ServiceConstants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentIntent(successPendingIntent)
                .setDeleteIntent(deletePendingIntent)
                .setCustomContentView(contentView);

        // Notify the manager about the notification given
        this.manager.notify(ServiceConstants.PET_NOTIFICATION_ID, builder.build());
    }
}
