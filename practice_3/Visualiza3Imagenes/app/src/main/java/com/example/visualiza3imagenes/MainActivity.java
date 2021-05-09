package com.example.visualiza3imagenes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchImages(View view) {
        Intent intent = new Intent(MainActivity.this, LoadImagesActivity.class);

        // Start activity
        startActivityForResult(intent, 0);
    }
}