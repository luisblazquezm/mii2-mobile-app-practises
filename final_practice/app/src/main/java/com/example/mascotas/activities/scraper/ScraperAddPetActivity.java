package com.example.mascotas.activities.scraper;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mascotas.R;
import com.example.mascotas.adapters.PetAdapter;
import com.example.mascotas.interfaces.Constants;
import com.example.mascotas.interfaces.ScrapperConstants;
import com.example.mascotas.models.Pet;
import com.example.mascotas.models.PetModel;
import com.example.mascotas.models.PetScraper;
import com.example.mascotas.utils.Utils;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScraperAddPetActivity extends AppCompatActivity {

    // ATTRIBUTES
    private Pet newPet;
    private String dateString;
    private String comment;
    private int requestCode;

    // UI ELEMENTS
    private EditText petNameEditText;
    private EditText petOwnerEditText;
    private RadioGroup petTypeRadioGroup;
    private CheckBox petIsVaccinatedCheckBox;
    private TextView adoptionDateTextView;
    private EditText petCommentEditText;
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
        setContentView(R.layout.activity_add_pet_scraper);

        // recovering the instance state
        if (savedInstanceState != null) {
            this.newPet = new Pet();

            this.newPet.setName(savedInstanceState.getString(ScrapperConstants.NAME_STATE_KEY));
            this.newPet.setOwner(savedInstanceState.getString(ScrapperConstants.OWNER_STATE_KEY));
            this.newPet.setVaccinated(savedInstanceState.getBoolean(ScrapperConstants.IS_VACCINATED_KEY));

            RadioButton rb = null;
            String radioText = Constants.DOG_VALUE;
            if (null != findViewById(savedInstanceState.getInt(ScrapperConstants.TYPE_STATE_KEY))) {
                rb = (RadioButton) findViewById(savedInstanceState.getInt(ScrapperConstants.TYPE_STATE_KEY));
                if (null != rb)
                    radioText = rb.getText().toString();
            }
            this.newPet.setType(radioText);

            this.newPet.setDate(this.newPet.parseFromStringToDate(
                    savedInstanceState.getString(ScrapperConstants.DATE_STATE_KEY)));

            // Optional
            this.comment = savedInstanceState.getString(ScrapperConstants.COMMENT_STATE_KEY);
        }

        // Get UI element references
        this.petNameEditText = (EditText) findViewById(R.id.scraperAddPetNameEditText);
        this.petOwnerEditText= (EditText) findViewById(R.id.scraperAddPetOwnerEditText);
        this.petTypeRadioGroup = (RadioGroup) findViewById(R.id.scraperAddPetTypeRadioGroup);
        this.petIsVaccinatedCheckBox = (CheckBox) findViewById(R.id.scraperAddPetTypeCheckbox);
        this.adoptionDateTextView = (TextView) findViewById(R.id.scraperAddPetDateTextView);
        this.petCommentEditText = (EditText) findViewById(R.id.scraperAddPetCommentEditText);
        this.petsListView =  (ListView) findViewById(R.id.petListViewAddScraperList);
        this.emptyTextView = (TextView) findViewById(R.id.emptyScraperAddTextView);
        this.loadingProgressBarr = (ProgressBar) findViewById(R.id.progressBarScraperAddLoading);

        // Set active empty text
        this.emptyTextView.setVisibility(View.VISIBLE);
        this.loadingProgressBarr.setVisibility(View.GONE);

        // Load models
        this.petModel = new PetModel();
        this.petScraper = new PetScraper();

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and the
        // array as a third parameter.
        this.petAdapter = new PetAdapter(this, (ArrayList<Pet>) this.petModel.getPetList());
        this.petsListView.setAdapter(this.petAdapter);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.petModel = savedInstanceState.getParcelable(ScrapperConstants.PET_SCRAPED_LIST_STATE_KEY);

        this.petNameEditText.setText(savedInstanceState.getString(ScrapperConstants.NAME_STATE_KEY));
        this.petOwnerEditText.setText(savedInstanceState.getString(ScrapperConstants.OWNER_STATE_KEY));
        this.petIsVaccinatedCheckBox.setChecked(savedInstanceState.getBoolean(ScrapperConstants.IS_VACCINATED_KEY));
        this.adoptionDateTextView.setText(savedInstanceState.getString(ScrapperConstants.DATE_STATE_KEY));
        this.petTypeRadioGroup.check(savedInstanceState.getInt(ScrapperConstants.TYPE_STATE_KEY));
        this.petCommentEditText.setText(savedInstanceState.getString(ScrapperConstants.COMMENT_STATE_KEY));

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and the
        // array as a third parameter.
        petAdapter = new PetAdapter(this, this.petModel.getPetList());

        // Update results of adapter with the list on userStorage
        this.petAdapter.updateResults(this.petModel.getPetList());
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ScrapperConstants.NAME_STATE_KEY, this.petNameEditText.getText().toString());
        outState.putString(ScrapperConstants.OWNER_STATE_KEY, this.petOwnerEditText.getText().toString());
        outState.putString(ScrapperConstants.DATE_STATE_KEY, this.adoptionDateTextView.getText().toString());
        outState.putString(ScrapperConstants.COMMENT_STATE_KEY, this.petCommentEditText.getText().toString());
        outState.putBoolean(ScrapperConstants.IS_VACCINATED_KEY, this.petIsVaccinatedCheckBox.isChecked());
        outState.putInt(ScrapperConstants.TYPE_STATE_KEY, this.petTypeRadioGroup.getCheckedRadioButtonId());
        outState.putParcelable(ScrapperConstants.PET_SCRAPED_LIST_STATE_KEY, this.petModel);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    public void changeDate(View view) {

        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Add event listener to display the datepicker with the current date set by default
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthofyear, int dayofmonth) {
                dateString = dayofmonth + "/" + monthofyear + "/" + year; // change attribute of a new person
                adoptionDateTextView.setText(dateString); // set in the text the new date
            }
        }, year, month, day);

        // Show and display datepicker when the button is pressed
        dpd.show();
    }

    private void clearFields() {
        this.petNameEditText.getText().clear();
        this.petOwnerEditText.getText().clear();
        this.petIsVaccinatedCheckBox.setChecked(false);
        this.adoptionDateTextView.setText(Utils.getCurrenDate());
        this.petTypeRadioGroup.check(R.id.dogRadioButton);
        this.petCommentEditText.getText().clear();
    }

    public void addPet(View view) {

        // Set empty text to gone
        if (View.VISIBLE == this.emptyTextView.getVisibility()) {
            this.emptyTextView.setVisibility(View.GONE);
        }

        // Set loading barr
        this.loadingProgressBarr.setVisibility(View.VISIBLE);

        // Get values from UI elements
        String name = this.petNameEditText.getText().toString();
        if (name.isEmpty())
            name = Constants.UNKNOWN_DEFAULT_VALUE;
        String owner = this.petOwnerEditText.getText().toString();
        if (owner.isEmpty())
            owner = Constants.UNKNOWN_DEFAULT_VALUE;
        String dateString = this.adoptionDateTextView.getText().toString();
        String comment = this.petCommentEditText.getText().toString();
        String type = "";
        RadioButton rb = (RadioButton) findViewById(this.petTypeRadioGroup.getCheckedRadioButtonId());
        if (null != rb)
            type = rb.getText().toString();
        if (type.isEmpty())
            type = Constants.UNKNOWN_DEFAULT_VALUE;
        boolean isVaccinated = this.petIsVaccinatedCheckBox.isChecked();
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        } catch (ParseException e) {
            date = new Date();
        }

        // Create new pet
        Pet p = new Pet(name, owner, type, date, isVaccinated);

        // Register in URL form with POST
        Pet addedPet = null;
        try {
            addedPet = this.petScraper.addPet(p, comment);

            // Set empty text to gone
            this.loadingProgressBarr.setVisibility(View.GONE);

            // Add new pet
            this.petModel.insertScraped(addedPet);

            // Notify adapter about the modification to update the data in list
            this.petAdapter.notifyDataSetChanged();

            // Clear all fields
            this.clearFields();
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), R.string.request_error_text, Toast.LENGTH_LONG).show();
        }
    }

    public void exit(View view) {
        finish();
    }
}
