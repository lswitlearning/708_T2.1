package com.example.task2_1p;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Spinner sourceUnitSpinner, destinationUnitSpinner, typeSpinner;
    private EditText sourceInput;
    private Button convertButton;
    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        sourceUnitSpinner = findViewById(R.id.sourceUnit);
        destinationUnitSpinner = findViewById(R.id.destinationUnit);
        typeSpinner = findViewById(R.id.type);
        sourceInput = findViewById(R.id.sourceInput);
        convertButton = findViewById(R.id.Convert);
        resultTextView = findViewById(R.id.result);

        // Clear result text view
        resultTextView.setText("");

        // Initialize spinner adapter for conversion types
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.conversion_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // Set item selected listener for conversion types spinner
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                if (selectedType.equals("Length") || selectedType.equals("Weight") || selectedType.equals("Temperature")) {
                    sourceInput.setEnabled(true);
                } else {
                    sourceInput.setEnabled(false);
                }

                // Retrieve unit arrays from resources
                String[] lengthUnits = getResources().getStringArray(R.array.length_units);
                String[] weightUnits = getResources().getStringArray(R.array.weight_units);
                String[] temperatureUnits = getResources().getStringArray(R.array.temperature_units);

                // Determine unit array based on selected type
                String[] units;
                if (selectedType.equals("Length")) {
                    units = lengthUnits;
                } else if (selectedType.equals("Weight")) {
                    units = weightUnits;
                } else {
                    units = temperatureUnits;
                }

                // Set adapter for source and destination unit spinners
                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, units);
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sourceUnitSpinner.setAdapter(unitAdapter);
                destinationUnitSpinner.setAdapter(unitAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get EditText content
                String inputText = sourceInput.getText().toString().trim();

                // check if null
                if (inputText.isEmpty() || !isValidDouble(inputText)) {
                    // if null, show error message
                    Toast.makeText(MainActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                } else {
                    convert();
                }
            }
        });
    }
    // Method to check if a string represents a valid positive double
    private boolean isValidDouble(String string) {
        try {
            // Try to parse the string as a double
            double value = Double.parseDouble(string);
            // Check if the parsed value is positive
            return value > 0;
        } catch (NumberFormatException e) {
            // If parsing fails, catch the exception and return false
            return false;
        }
    }

    // Method to perform conversion
    private void convert() {
        String type = typeSpinner.getSelectedItem().toString();
        String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
        String destinationUnit = destinationUnitSpinner.getSelectedItem().toString();

        double inputValue = Double.parseDouble(sourceInput.getText().toString());
        double resultValue = convertValue(inputValue, sourceUnit, destinationUnit, type);
        resultTextView.setText(formatResult(inputValue, sourceUnit, resultValue, destinationUnit));
    }

    // Method to perform specific conversion based on type
    private double convertValue(double inputValue, String sourceUnit, String destinationUnit, String type) {
        double resultValue = 0;

        switch (type) {
            case "Length":
                resultValue = convertLength(inputValue, sourceUnit, destinationUnit);
                break;
            case "Weight":
                resultValue = convertWeight(inputValue, sourceUnit, destinationUnit);
                break;
            case "Temperature":
                resultValue = convertTemperature(inputValue, sourceUnit, destinationUnit);
                break;
        }

        return resultValue;
    }

    // Method to convert length
    private double convertLength(double value, String sourceUnit, String destinationUnit) {
        double resultValue = 0;

        // Length conversion logic
        // Switch cases based on source unit
        switch (sourceUnit) {
            case "inch":
                switch (destinationUnit) {
                    case "inch":
                        resultValue = value;
                        break;
                    case "foot":
                        resultValue = value / 12;
                        break;
                    case "yard":
                        resultValue = value / 36;
                        break;
                    case "mile":
                        resultValue = value / 63360;
                        break;
                    case "cm":
                        resultValue = value * 2.54;
                        break;
                    case "km":
                        resultValue = value * 0.0000254;
                        break;
                }
                break;
            case "foot":
                switch (destinationUnit) {
                    case "inch":
                        resultValue = value * 12;
                        break;
                    case "foot":
                        resultValue = value;
                        break;
                    case "yard":
                        resultValue = value / 3;
                        break;
                    case "mile":
                        resultValue = value / 5280;
                        break;
                    case "cm":
                        resultValue = value * 30.48;
                        break;
                    case "km":
                        resultValue = value * 0.0003048;
                        break;
                }
                break;
            case "yard":
                switch (destinationUnit) {
                    case "inch":
                        resultValue = value * 36;
                        break;
                    case "foot":
                        resultValue = value * 3;
                        break;
                    case "yard":
                        resultValue = value;
                        break;
                    case "mile":
                        resultValue = value / 1760;
                        break;
                    case "cm":
                        resultValue = value * 91.44;
                        break;
                    case "km":
                        resultValue = value * 0.0009144;
                        break;
                }
                break;
            case "mile":
                switch (destinationUnit) {
                    case "inch":
                        resultValue = value * 63360;
                        break;
                    case "foot":
                        resultValue = value * 5280;
                        break;
                    case "yard":
                        resultValue = value * 1760;
                        break;
                    case "mile":
                        resultValue = value;
                        break;
                    case "cm":
                        resultValue = value * 160934.4;
                        break;
                    case "km":
                        resultValue = value * 1.60934;
                        break;
                }
                break;
            case "cm":
                switch (destinationUnit) {
                    case "inch":
                        resultValue = value * 0.393701;
                        break;
                    case "foot":
                        resultValue = value * 0.0328084;
                        break;
                    case "yard":
                        resultValue = value * 0.0109361;
                        break;
                    case "mile":
                        resultValue = value * 0.0000062137;
                        break;
                    case "cm":
                        resultValue = value;
                        break;
                    case "km":
                        resultValue = value * 0.00001;
                        break;
                }
                break;
            case "km":
                switch (destinationUnit) {
                    case "inch":
                        resultValue = value * 39370.1;
                        break;
                    case "foot":
                        resultValue = value * 3280.84;
                        break;
                    case "yard":
                        resultValue = value * 1093.61;
                        break;
                    case "mile":
                        resultValue = value * 0.621371;
                        break;
                    case "cm":
                        resultValue = value * 100000;
                        break;
                    case "km":
                        resultValue = value;
                        break;
                }
                break;
        }
        return resultValue;
    }

    // Method to convert weight
    private double convertWeight(double value, String sourceUnit, String destinationUnit) {
        double resultValue = 0;

        // Weight conversion logic
        // Handle cases for sourceUnit and destinationUnit
        switch (sourceUnit) {
            case "pound":
                switch (destinationUnit) {
                    case "pound":
                        resultValue = value;
                        break;
                    case "ounce":
                        resultValue = value * 16; // 1 pound = 16 ounces
                        break;
                    case "ton":
                        resultValue = value / 2000; // 1 pound = 0.0005 ton
                        break;
                    case "kg":
                        resultValue = value * 0.453592; // 1 pound = 0.453592 kg
                        break;
                    case "g":
                        resultValue = value * 453.592; // 1 pound = 453.592 g
                        break;
                }
                break;
            case "ounce":
                switch (destinationUnit) {
                    case "pound":
                        resultValue = value / 16; // 1 ounce = 1/16 pounds
                        break;
                    case "ounce":
                        resultValue = value;
                        break;
                    case "ton":
                        resultValue = value / 32000; // 1 ounce = 0.00003125 ton
                        break;
                    case "kg":
                        resultValue = value * 0.0283495; // 1 ounce = 0.0283495 kg
                        break;
                    case "g":
                        resultValue = value * 28.3495; // 1 ounce = 28.3495 g
                        break;
                }
                break;
            case "ton":
                switch (destinationUnit) {
                    case "pound":
                        resultValue = value * 2000; // 1 ton = 2000 pounds
                        break;
                    case "ounce":
                        resultValue = value * 32000; // 1 ton = 32000 ounces
                        break;
                    case "ton":
                        resultValue = value;
                        break;
                    case "kg":
                        resultValue = value * 907.185; // 1 ton = 907.185 kg
                        break;
                    case "g":
                        resultValue = value * 907185; // 1 ton = 907185 g
                        break;
                }
                break;
            case "kg":
                switch (destinationUnit) {
                    case "pound":
                        resultValue = value / 0.453592; // 1 kg = 2.20462 pounds
                        break;
                    case "ounce":
                        resultValue = value / 0.0283495; // 1 kg = 35.274 ounces
                        break;
                    case "ton":
                        resultValue = value / 907.185; // 1 kg = 0.00110231 tons
                        break;
                    case "kg":
                        resultValue = value;
                        break;
                    case "g":
                        resultValue = value * 1000; // 1 kg = 1000 g
                        break;
                }
                break;
            case "g":
                switch (destinationUnit) {
                    case "pound":
                        resultValue = value / 453.592; // 1 g = 0.00220462 pounds
                        break;
                    case "ounce":
                        resultValue = value / 28.3495; // 1 g = 0.035274 ounces
                        break;
                    case "ton":
                        resultValue = value / 907185; // 1 g = 0.00000110231 tons
                        break;
                    case "kg":
                        resultValue = value / 1000; // 1 g = 0.001 kg
                        break;
                    case "g":
                        resultValue = value;
                        break;
                }
                break;
        }
        return resultValue;
    }

    // Method to convert weight
    private double convertTemperature(double value, String sourceUnit, String destinationUnit) {
        double resultValue = 0;

        switch (sourceUnit) {
            case "Celsius":
                switch (destinationUnit) {
                    case "Celsius":
                        resultValue = value;
                        break;
                    case "Fahrenheit":
                        resultValue = (value * 9 / 5) + 32; // Celsius to Fahrenheit conversion formula
                        break;
                    case "Kelvin":
                        resultValue = value + 273.15; // Celsius to Kelvin conversion formula
                        break;
                }
                break;
            case "Fahrenheit":
                switch (destinationUnit) {
                    case "Celsius":
                        resultValue = (value - 32) * 5 / 9; // Fahrenheit to Celsius conversion formula
                        break;
                    case "Fahrenheit":
                        resultValue = value;
                        break;
                    case "Kelvin":
                        resultValue = (value - 32) * 5 / 9 + 273.15; // Fahrenheit to Kelvin conversion formula
                        break;
                }
                break;
            case "Kelvin":
                switch (destinationUnit) {
                    case "Celsius":
                        resultValue = value - 273.15; // Kelvin to Celsius conversion formula
                        break;
                    case "Fahrenheit":
                        resultValue = (value - 273.15) * 9 / 5 + 32; // Kelvin to Fahrenheit conversion formula
                        break;
                    case "Kelvin":
                        resultValue = value;
                        break;
                }
                break;
        }

        return resultValue;
    }

    //formatting result
    private String formatResult(double inputValue, String sourceUnit, double resultValue, String destinationUnit) {
        DecimalFormat df = new DecimalFormat("#.####");
        return "Result: " + inputValue + " " + sourceUnit + " = " + df.format(resultValue) + " " + destinationUnit;
    }
}









