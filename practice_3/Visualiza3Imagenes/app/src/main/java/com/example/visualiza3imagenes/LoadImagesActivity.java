package com.example.visualiza3imagenes;

import androidx.annotation.MainThread;
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
import java.util.concurrent.CountDownLatch;

public class LoadImagesActivity extends AppCompatActivity {

    // UI XML ELEMENTS
    private ImageView imageView;
    private ProgressBar loadImageProgressBarr;
    private TextView loadImageTextLabel;
    private TextView endMessageTextView;

    // CONSTANTS
    private final int[] imageIds = {0, 1, 2};
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
        this.loadImageProgressBarr = (ProgressBar) findViewById(R.id.loadImageProgressBarr);
        this.loadImageTextLabel = (TextView) findViewById(R.id.loadImageMessageTextView);
        this.endMessageTextView = (TextView) findViewById(R.id.endMessageTextView);

        // Initialize progress barr
        loadImageProgressBarr.setMax(100);
        loadImageProgressBarr.setProgress(0);

        // Hide image view and message
        this.imageView.setVisibility(View.GONE);
        this.endMessageTextView.setVisibility(View.GONE);

        // Main thread before loading images
        loadImageProgressBarr.setVisibility(View.VISIBLE);
        loadImageTextLabel.setVisibility(View.VISIBLE);

        // Ask secondary threads to load images
        this.getImgFromThread();
    }

    private void getImgFromThread() {

        // Initialization and Start threads
        for (int i = 0; i < this.imageIds.length; i++) {
            LoadImagesThread th = new LoadImagesThread(this.imageIds[i]);
            th.start();
        }
    }

    class LoadImagesThread extends Thread{
        private int i;
        private int imageId;

        public LoadImagesThread(int imageId) {
            this.imageId = imageId;
        }

        public void setImg() {

            // Hide load label and spinner
            loadImageProgressBarr.setVisibility(View.GONE);
            loadImageTextLabel.setVisibility(View.GONE);

            // Display imageView hidden
            imageView.setVisibility(View.VISIBLE);

            Log.w("setImg", "Load image: " + this.imageId);

            // Set image depending on the image
            if (DOG_IMG_ID == this.imageId)
                imageView.setImageResource(R.drawable.perro);
            else if (CAT_IMG_ID == this.imageId)
                imageView.setImageResource(R.drawable.gato);
            else if (LION_IMG_ID == this.imageId)
                imageView.setImageResource(R.drawable.leon);
            else
                imageView.setImageResource(R.drawable.perro);

            Log.w("setImg", "Wait 3 SECONDS");

            // Wait for every second
            try {
                Thread.sleep(IMAGE_VISUALIZATION_TIME); // 1 second
            } catch (Exception ex) {
            }

            Log.w("setImg", "Finish visualization");

            Log.w("doInBackground", "imageIdToShow is " + this.imageId);

            // Hide image view
            imageView.setVisibility(View.GONE);

            if (this.imageId >= imageIds.length) {
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

        private void publishProgress(int progressValue, int randomTime) {
            int progress = progressValue * 100 / randomTime;

            loadImageProgressBarr.setProgress(progress * 10);
        }

        @Override
        public void run() {

            // Generate aleatory number
            int loadAleatoryTime = new Random().nextInt((MAX_ALEATORY_NUMBER - MIN_ALEATORY_NUMBER) + 1) + MIN_ALEATORY_NUMBER;

            // Make thread simulate is loading the image
            for (i = 0; i <= loadAleatoryTime; i++) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishProgress(i, loadAleatoryTime);
                    }
                });
                // Wait for every second
                try {
                    Thread.sleep(MILLISECOND_TIME); // 1 second
                } catch (Exception ex) {
                }
            }

            // Set barr when thread finish
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    publishProgress(loadAleatoryTime, loadAleatoryTime);
                    setImg();
                }
            });
        }
    }

}