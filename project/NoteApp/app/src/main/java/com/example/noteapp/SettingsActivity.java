package com.example.noteapp;


import androidx.appcompat.app.AppCompatActivity;
import com.clorem.db.Clorem;
import com.clorem.db.Node;

import android.app.UiModeManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Node root, settings;

    Spinner styleSpinner, colorSpinner;
    TextView textView3, textView4;
    CheckBox darkmodeCheckBox;
    Button saveButton;
    UiModeManager uiModeManager;
    Preferences preferences;

    private class Preferences {
        String style = "sans-serif";
        String color = "black";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        root = Clorem.getInstance(this, "noteDB").getDatabase();
        settings = root.node("settings");

        preferences = getPreferences(settings);

        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        textView3.setTextColor(Color.parseColor(preferences.color));

        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        textView4.setTextColor(Color.parseColor(preferences.color));

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        saveButton.setTextColor(Color.parseColor(preferences.color));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.put("style", preferences.style);
                settings.put("color", preferences.color).commit();

                Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();

                recreate();
            }
        });

        styleSpinner = (Spinner) findViewById(R.id.styleSpinner);


        colorSpinner = (Spinner) findViewById(R.id.colorSpinner);


        ArrayAdapter<CharSequence> stylesAdapter = ArrayAdapter.createFromResource(this, R.array.styles, android.R.layout.simple_spinner_dropdown_item);
        stylesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleSpinner.setAdapter(stylesAdapter);
        styleSpinner.setSelection(stylesAdapter.getPosition(preferences.style));

        styleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                preferences.style = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_dropdown_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setSelection(colorAdapter.getPosition(preferences.color));

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                preferences.color = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Preferences getPreferences(Node settings) {
        Preferences preferences = new Preferences();

        String style = settings.getString("style", "sans-serif");
        String color = settings.getString("color", "black");

        preferences.style = style;
        preferences.color = color;

        return preferences;
    }
}