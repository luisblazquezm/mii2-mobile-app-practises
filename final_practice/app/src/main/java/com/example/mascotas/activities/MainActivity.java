package com.example.mascotas.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mascotas.R;
import com.example.mascotas.activities.scraper.ScraperAddPetActivity;
import com.example.mascotas.activities.scraper.ScraperListPetsActivity;
import com.example.mascotas.interfaces.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add connectivity manager for notifications
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get active notifications network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.getState().toString().equalsIgnoreCase(Constants.CONNECTION_ESTABLISHED_STATE)){
            Toast.makeText(this.getApplicationContext(), R.string.no_connection_text, Toast.LENGTH_LONG);
        }
    }

    public void start(View view) {
        Intent intent = new Intent(this, PetListActivity.class);
        startActivityForResult(intent, 0);
    }

    public void addPetFromScraper(View view) {
        Intent intent = new Intent(this, ScraperAddPetActivity.class);
        startActivityForResult(intent, 0);
    }

    public void listPetsFromScraper(View view) {
        Intent intent = new Intent(this, ScraperListPetsActivity.class);
        startActivityForResult(intent, 0);
    }
}