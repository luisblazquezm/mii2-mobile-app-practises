package com.example.visualiza3imagenes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class LoadImagesActivity extends AppCompatActivity {

    // UI XML ELEMENTS
    private ImageView imageView;
    private ProgressBar loadImageProgressBarr;
    private TextView loadImageTextLabel;
    private Handler progressBarbHandler = new Handler();
    private TextView endMessageTextView;

    // CONSTANTS
    private final int[] imageIds = {0, 1, 2};
    private int progressBarStatus = 0;
    private int imageIdToShow = -1;
    private final int DOG_IMG_ID = 0;
    private final int CAT_IMG_ID = 1;
    private final int LION_IMG_ID = 2;
    private final int MIN_ALEATORY_NUMBER = 1;
    private final int MAX_ALEATORY_NUMBER = 5;
    private final long MILLISECOND_TIME = 1000; // 1 second
    private final long IMAGE_VISUALIZATION_TIME = 3 * MILLISECOND_TIME; // 3 seconds in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_imagenes_y_fin2);

        // Load UI element to load images
        this.imageView = (ImageView) findViewById(R.id.imageViewCatDogAndLion);
        this.loadImageProgressBarr = (ProgressBar) findViewById(R.id.loadImageProgressBar);
        this.loadImageTextLabel = (TextView) findViewById(R.id.loadImageMessageTextView);
        this.endMessageTextView = (TextView) findViewById(R.id.endMessageTextView);

        // Hide image view and message
        this.imageView.setVisibility(View.GONE);
        this.endMessageTextView.setVisibility(View.GONE);

        // Ask secondary threads to load images
        this.loadBarr();
    }

    public void setImg(int imageIdToShow) {

        // Hide load label and spinner
        loadImageProgressBarr.setVisibility(View.GONE);
        loadImageTextLabel.setVisibility(View.GONE);

        // Display imageView hidden
        imageView.setVisibility(View.VISIBLE);

        Log.w("setImg", "Load image: " + imageIdToShow);

        // Set image depending on the image
        if (DOG_IMG_ID == imageIdToShow)
            imageView.setImageResource(R.drawable.perro);
        else if (CAT_IMG_ID == imageIdToShow)
            imageView.setImageResource(R.drawable.gato);
        else if (LION_IMG_ID == imageIdToShow)
            imageView.setImageResource(R.drawable.leon);
        else
            imageView.setImageResource(R.drawable.perro);

        Log.w("setImg", "Wait 3 SECONDS");

        // Visualize during 3 seconds
        try {
            Thread.sleep(IMAGE_VISUALIZATION_TIME); // 3 seconds
        } catch (Exception ex) {
        }

        Log.w("setImg", "Finish visualization");

        Log.w("doInBackground", "imageIdToShow is " + imageIdToShow);

        // Hide image view
        imageView.setVisibility(View.GONE);

        if (imageIdToShow >= imageIds.length) {
            // Main thread ends tasks
            loadImageProgressBarr.setVisibility(View.GONE);
            loadImageTextLabel.setVisibility(View.GONE);
            endMessageTextView.setVisibility(View.VISIBLE);
        } else {
            // Display load image label and progress barr
            loadImageProgressBarr.setVisibility(View.VISIBLE);
            loadImageTextLabel.setVisibility(View.VISIBLE);
        }
    }

    public void loadBarr() {

        // Main thread before loading images
        loadImageProgressBarr.setVisibility(View.VISIBLE);
        loadImageTextLabel.setVisibility(View.VISIBLE);

        //for (int imageId : imageIds){

            Thread th = new HandlerThread("UIHandler"){

                @Override
                public void run() {
                    // Generate aleatory number
                    int loadAleatoryTime = new Random().nextInt((MAX_ALEATORY_NUMBER - MIN_ALEATORY_NUMBER) + 1) + MIN_ALEATORY_NUMBER;

                    // Set max and min for barr
                    runOnUiThread(new Runnable() {
                      public void run() {
                          loadImageProgressBarr.setMax(loadAleatoryTime);
                          loadImageProgressBarr.setProgress(0);
                      }
                    });

                    // Make thread simulate is loading the image
                    for (progressBarStatus = 0; progressBarStatus <= loadAleatoryTime; progressBarStatus++) {

                        progressBarbHandler.post(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        loadImageProgressBarr.setProgress(progressBarStatus);
                                    }
                                });
                            }
                        });

                        // Wait for every second
                        try {
                            Thread.sleep(MILLISECOND_TIME); // 1 second
                        } catch (Exception ex) {
                        }
                        Log.w("doInBackground", "Progress is " + progressBarStatus + " / " + loadAleatoryTime);
                    }

                    //imageIdToShow = imageId;

                    // Reset status
                    progressBarStatus = 0;
                }
            };

        //}

        // Start thread
        th.start();

        // Wait for thread to finish
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Load image
        setImg(0);
    }

}