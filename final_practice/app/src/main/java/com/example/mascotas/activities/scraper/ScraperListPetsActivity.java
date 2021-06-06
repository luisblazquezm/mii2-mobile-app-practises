package com.example.mascotas.activities.scraper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mascotas.R;
import com.example.mascotas.activities.PetListActivity;
import com.example.mascotas.adapters.PetAdapter;
import com.example.mascotas.interfaces.Constants;
import com.example.mascotas.interfaces.ScrapperConstants;
import com.example.mascotas.models.Pet;
import com.example.mascotas.models.PetModel;
import com.example.mascotas.models.PetScraper;

import java.util.ArrayList;

public class ScraperListPetsActivity extends AppCompatActivity {

    // UI ELEMENTS
    private TextView minLimitTextView;
    private TextView maxLimitTextView;
    private ListView petsListView;
    private TextView emptyTextView;
    private ProgressBar loadingProgressBarr = null;

    // OTHERS
    private PetScraper petScraper = null;
    private PetModel petModel = null;
    private PetAdapter petAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pet_scraper);

        // Load models
        this.petModel = new PetModel();
        this.petScraper = new PetScraper();

        // Get UI elements references
        this.petsListView =  (ListView) findViewById(R.id.petListViewScraperList);
        this.minLimitTextView = (TextView) findViewById(R.id.minLimitEditText);
        this.maxLimitTextView = (TextView) findViewById(R.id.maxLimitEditText);
        this.emptyTextView = (TextView) findViewById(R.id.emptyScraperListTextView);
        this.loadingProgressBarr = (ProgressBar) findViewById(R.id.progressBarScraperListLoading);

        // Set active empty text
        this.emptyTextView.setVisibility(View.VISIBLE);
        this.loadingProgressBarr.setVisibility(View.GONE);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and the
        // array as a third parameter.
        this.petAdapter = new PetAdapter(ScraperListPetsActivity.this, ScraperListPetsActivity.this.petModel.getPetList());
        this.petsListView.setAdapter(this.petAdapter);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.petModel = savedInstanceState.getParcelable(ScrapperConstants.PET_SCRAPED_LIST_STATE_KEY);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and the
        // array as a third parameter.
        petAdapter = new PetAdapter(ScraperListPetsActivity.this, ScraperListPetsActivity.this.petModel.getPetList());

        // Update results of adapter with the list on userStorage
        this.petAdapter.updateResults(this.petModel.getPetList());

        // Set active empty text
        this.emptyTextView.setVisibility(View.GONE);
        this.loadingProgressBarr.setVisibility(View.GONE);
        this.petsListView.setAdapter(this.petAdapter);
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ScrapperConstants.PET_SCRAPED_LIST_STATE_KEY, this.petModel);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    public void scrapeListPets(View view){

        int minId, maxId;

        // Set empty text to gone
        if (View.VISIBLE == this.emptyTextView.getVisibility()) {
            this.emptyTextView.setVisibility(View.GONE);
        }

        // Set loading barr
        this.loadingProgressBarr.setVisibility(View.VISIBLE);

        // Get value from max and min inputs
        try {
            minId = Integer.parseInt(this.minLimitTextView.getText().toString());
            maxId = Integer.parseInt(this.maxLimitTextView.getText().toString());
        }catch (Exception e){
            Toast.makeText(this,R.string.bad_parameters_text, Toast.LENGTH_LONG).show();
            return;
        }

        // Check values
        if(minId >= maxId){
            Toast.makeText(this,R.string.limit_error_text, Toast.LENGTH_LONG).show();
            return;
        }

        // Scrape content with the post values given
        ArrayList<Pet> scrapedPets = null;
        try {
            // Scrape pets
            scrapedPets = this.petScraper.getPetList(minId, maxId);

            // Set empty text to gone
            this.loadingProgressBarr.setVisibility(View.GONE);

            // Add new person
            Log.d("d", "The size of the list BEFORE insertAllScraped " + this.petModel.getPetList().size());
            petModel.insertAllScraped(scrapedPets);
            Log.d("d", "The size of the list AFTER insertAllScraped " + this.petModel.getPetList().size());

            // Notify adapter about the modification to update the data in list
            this.petAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), R.string.request_error_text, Toast.LENGTH_LONG).show();
        }
    }

    public void exit(View view) {
        finish();
    }
}
