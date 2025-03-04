package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private Spinner conversionTypeSpinner, sourceUnitSpinner, destinationUnitSpinner;
    private EditText inputValue;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Elements
        conversionTypeSpinner = findViewById(R.id.conversionTypeSpinner);
        sourceUnitSpinner = findViewById(R.id.sourceUnitSpinner);
        destinationUnitSpinner = findViewById(R.id.destinationUnitSpinner);
        inputValue = findViewById(R.id.valueEditText);
        resultText = findViewById(R.id.result_text);
        Button convertButton = findViewById(R.id.convertButton);

        // Set up conversion type spinner
        setupConversionTypeSpinner();

        // Set up Convert button click listener
        convertButton.setOnClickListener(v -> performConversion());
    }

    // Function to setup Conversion Type Spinner
    private void setupConversionTypeSpinner() {
        String[] conversionTypes = {"Length", "Weight", "Temperature"};
        setupSpinner(conversionTypeSpinner, conversionTypes);

        conversionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Length
                        setupUnits(new String[]{"Centimeter (cm)", "Meter (m)", "Kilometer (km)",
                                "Foot (ft)", "Yard (yd)", "Mile (mi)", "Inch (in)"}, 0);
                        break;
                    case 1: // Weight
                        setupUnits(new String[]{"Gram (g)", "Kilogram (kg)", "Pound (lb)",
                                "Ounce (oz)", "Ton (t)"}, 2);
                        break;
                    case 2: // Temperature
                        setupUnits(new String[]{"Celsius (°C)", "Fahrenheit (°F)", "Kelvin (K)"}, 0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        conversionTypeSpinner.setSelection(0);
    }

    // Function to setup Source & Destination Spinners
    private void setupUnits(String[] units, int sourceDefault) {
        setupSpinner(sourceUnitSpinner, units);
        setupSpinner(destinationUnitSpinner, units);
        sourceUnitSpinner.setSelection(sourceDefault);
        destinationUnitSpinner.setSelection(1);
    }

    // Helper function to setup a Spinner
    private void setupSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    // Function to Perform Conversion
    private void performConversion() {
        String inputText = inputValue.getText().toString();

        if (inputText.isEmpty()) {
            Toast.makeText(this, "Enter a value to convert", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputNumber = Double.parseDouble(inputText);
        String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
        String destinationUnit = destinationUnitSpinner.getSelectedItem().toString();
        String conversionType = conversionTypeSpinner.getSelectedItem().toString();

        double result = convertUnit(conversionType, sourceUnit, destinationUnit, inputNumber);

        // Format result and display it
        resultText.setText(formatResult(result, destinationUnit));
    }
    private String formatResult(double result, String destinationUnit) {
        DecimalFormat df = new DecimalFormat("0.####");
        String formattedResult = df.format(result);

        // Extract unit symbol (if exists)
        String unitSymbol = extractUnitSymbol(destinationUnit);

        return formattedResult + " " + unitSymbol;
    }

    // Extracts unit symbol from text (if available)
    private String extractUnitSymbol(String unitText) {
        if (unitText.contains("(") && unitText.contains(")")) {
            return unitText.substring(unitText.indexOf("(") + 1, unitText.indexOf(")"));
        }
        return unitText;
    }


    // Function to Convert Units
    private double convertUnit(String conversionType, String sourceUnit, String destinationUnit, double value) {
        switch (conversionType) {
            case "Length":
                return convertLength(sourceUnit, destinationUnit, value);
            case "Weight":
                return convertWeight(sourceUnit, destinationUnit, value);
            case "Temperature":
                return convertTemperature(sourceUnit, destinationUnit, value);
            default:
                return value;
        }
    }

    private double convertLength(String source, String dest, double value) {
        double meters = value;

        // Convert source unit to meters
        switch (source) {
            case "Centimeter (cm)":
                meters = value / 100;
                break;
            case "Kilometer (km)":
                meters = value * 1000;
                break;
            case "Foot (ft)":
                meters = value * 0.3048;
                break;
            case "Yard (yd)":
                meters = value * 0.9144;
                break;
            case "Mile (mi)":
                meters = value * 1609.34;
                break;
            case "Inch (in)":
                meters = value * 0.0254;
                break;
        }

        // Convert meters to destination unit
        switch (dest) {
            case "Centimeter (cm)":
                return meters * 100;
            case "Kilometer (km)":
                return meters / 1000;
            case "Foot (ft)":
                return meters / 0.3048;
            case "Yard (yd)":
                return meters / 0.9144;
            case "Mile (mi)":
                return meters / 1609.34;
            case "Inch (in)":
                return meters / 0.0254;
        }

        return meters; // If "Meter (m)", return unchanged
    }
    private double convertWeight(String source, String dest, double value) {
        double kilograms = value;

        // Convert source unit to kilograms
        switch (source) {
            case "Gram (g)":
                kilograms = value / 1000;
                break;
            case "Pound (lb)":
                kilograms = value * 0.453592;
                break;
            case "Ounce (oz)":
                kilograms = value * 0.0283495;
                break;
            case "Ton (t)":
                kilograms = value * 907.185;
                break;
        }

        // Convert kilograms to destination unit
        switch (dest) {
            case "Gram (g)":
                return kilograms * 1000;
            case "Pound (lb)":
                return kilograms / 0.453592;
            case "Ounce (oz)":
                return kilograms / 0.0283495;
            case "Ton (t)":
                return kilograms / 907.185;
        }

        return kilograms; // If "Kilogram (kg)", return unchanged
    }
    private double convertTemperature(String source, String dest, double value) {
        double celsius = value;

        // Convert source unit to Celsius
        switch (source) {
            case "Fahrenheit (°F)":
                celsius = (value - 32) / 1.8;
                break;
            case "Kelvin (K)":
                celsius = value - 273.15;
                break;
        }
        // Convert Celsius to destination unit
        switch (dest) {
            case "Fahrenheit (°F)":
                return (celsius * 1.8) + 32;
            case "Kelvin (K)":
                return celsius + 273.15;
        }

        return celsius; // If "Celsius (°C)", return unchanged
    }




}
