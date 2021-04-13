package com.example.proyectosuma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText firstOperandTextView;
    private EditText secondOperandTextView;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get view elements
        this.firstOperandTextView = (EditText) findViewById(R.id.firstOperandEditText);
        this.secondOperandTextView = (EditText) findViewById(R.id.secondOperandEditText);
        this.resultTextView = (TextView) findViewById(R.id.resultTextView);
    }

    public void addFunction(View view) {

        // Get values from inputs
        String firstOperandText = this.firstOperandTextView.getText().toString();
        String secondOperandText = this.secondOperandTextView.getText().toString();

        String result;
        // Check values if empty and o adding operation
        if (!firstOperandText.isEmpty() && !secondOperandText.isEmpty()) {
            int firstOperand = Integer.parseInt(firstOperandText);
            int secondOperand = Integer.parseInt(secondOperandText);
            int tempResult = firstOperand + secondOperand;
            result = String.valueOf(tempResult);
        } else {
            result = "Nan";
        }

        // Set result
        this.resultTextView.setText(result);
    }
}