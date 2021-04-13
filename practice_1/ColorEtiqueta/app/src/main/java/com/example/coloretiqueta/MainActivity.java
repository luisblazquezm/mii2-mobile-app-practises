package com.example.coloretiqueta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void execute(View view) {

        // Get view elements
        TextView messageTextView = (TextView) findViewById(R.id.messageTextView);
        RadioGroup colorRadioGroup = (RadioGroup) findViewById(R.id.colorRadioGroup);
        RadioGroup typeRadioGroup = (RadioGroup) findViewById(R.id.typeRadioGroup);
        CheckBox visibilityCheckbox = (CheckBox) findViewById(R.id.visibilityCheckBox);

        // Check active button in color group
        int selectedTypeButtonId = typeRadioGroup.getCheckedRadioButtonId();

        // Check active button in type group
        int selectedColorButtonId = colorRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedColorRadioButton = (RadioButton) findViewById(selectedColorButtonId);

        // Check visibility checkbox
        boolean isMessageVisible = visibilityCheckbox.isChecked();

        // Set colors for background and text
        if (selectedTypeButtonId == R.id.backgroundColorButton) // background selected
            messageTextView.setBackgroundColor(selectedColorRadioButton.getCurrentTextColor());
        else // message text selected
            messageTextView.setTextColor(selectedColorRadioButton.getCurrentTextColor());

        // Set message visibility
        if (isMessageVisible)
            messageTextView.setVisibility(View.VISIBLE);
        else
            messageTextView.setVisibility(View.INVISIBLE);
    }
}