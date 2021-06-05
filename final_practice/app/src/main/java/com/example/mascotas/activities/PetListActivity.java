package com.example.mascotas.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mascotas.R;
import com.example.mascotas.adapters.PetAdapter;
import com.example.mascotas.interfaces.Constants;
import com.example.mascotas.models.PetModel;
import com.example.mascotas.services.PetService;

import org.w3c.dom.Text;

public class PetListActivity extends Activity {

    // UI ELEMENTS
    private ListView listView = null;
    private Button runButton = null;
    private static TextView numEventsTextView = null;
    private static TextView numProcessesTextView = null;
    private TextView emptyTextView = null;
    private LinearLayout textLabelsCounterLayout = null;
    private ProgressBar loadingProgressBarr = null;

    // CONSTANTS
    private final int LOAD_TIME_IN_MILLISECONDS = 5 * 1000; // 5 seconds

    // OTHERS
    private PetModel petModel = null;
    private static PetAdapter listViewArrayAdapter = null;
    private boolean hasServiceStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        // Initialize storage
        this.petModel = new PetModel();

        // Initialize flag
        this.hasServiceStarted = false;

        // Capture the layout's TextView and set the string as its text
        this.listView = (ListView) findViewById(R.id.petListView);

        // Get UI elements
        this.runButton = (Button) findViewById(R.id.runEventsButton);
        this.numEventsTextView = (TextView) findViewById(R.id.numEventsTextView);
        this.numProcessesTextView = (TextView) findViewById(R.id.numProcessesTextView);
        this.emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        this.loadingProgressBarr = (ProgressBar) findViewById(R.id.progressBarLoading);
        this.textLabelsCounterLayout = (LinearLayout) findViewById(R.id.textLabelsCounterLayout);

        // Set UI elements invisible
        this.runButton.setVisibility(View.GONE);
        this.textLabelsCounterLayout.setVisibility(View.GONE);
        this.loadingProgressBarr.setVisibility(View.VISIBLE);
        this.emptyTextView.setVisibility(View.VISIBLE);

        // Start app when start activity
        this.startApp();
    }

    public static void updateListViewNotification(){
        PetListActivity.listViewArrayAdapter.notifyDataSetChanged();
        PetListActivity.numEventsTextView.setText(Integer.valueOf(PetModel.build().getEvents()).toString());
        PetListActivity.numProcessesTextView.setText(Integer.valueOf(PetModel.build().getProcessed()).toString());
    }

    public void runEventsService(View view) {
        // Check if service has started to start service
        if (this.hasServiceStarted) {
            this.hasServiceStarted = false;
            this.runButton.setText(R.string.start_service_button_text); // Change text of button
            stopService(new Intent(PetListActivity.this, PetService.class));
        } else {
            this.hasServiceStarted = true;
            this.runButton.setText(R.string.stop_service_button_text); // Change text of button
            startService(new Intent(PetListActivity.this, PetService.class));
        }
    }

    private void startApp() {

        // Fill listview with a thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Load pet data from URL
                PetListActivity.this.petModel.loadFromURL(Constants.DEFAULT_JSON_PET_URL);

                // Simulate loading
                try {
                    Thread.sleep(LOAD_TIME_IN_MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Hide loading progress barr
                        loadingProgressBarr.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.GONE);

                        // Show buttons
                        runButton.setVisibility(View.VISIBLE);
                        textLabelsCounterLayout.setVisibility(View.VISIBLE);

                        // This is the array adapter, it takes the context of the activity as a
                        // first parameter, the type of list view as a second parameter and the
                        // array as a third parameter.
                        listViewArrayAdapter = new PetAdapter(PetListActivity.this, PetListActivity.this.petModel.getList());
                        listView.setAdapter(listViewArrayAdapter); // set adapter
                    }
                });
            }
        }).start();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.petModel = savedInstanceState.getParcelable(Constants.PET_LIST_STATE_KEY);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and the
        // array as a third parameter.
        listViewArrayAdapter = new PetAdapter(PetListActivity.this, PetListActivity.this.petModel.getList());

        // Update results of adapter with the list on userStorage
        this.listViewArrayAdapter.updateResults(this.petModel.getList());
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.PET_LIST_STATE_KEY, this.petModel);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    public void exit(View view) {
        finish();
    }
}
