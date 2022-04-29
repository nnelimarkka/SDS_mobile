package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.clorem.db.Clorem;
import com.clorem.db.Node;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Node root;
    Node settings;
    Node notes;

    ListView notesListView;
    EditText noteEditText;
    EditText titleEditText;
    Button submitButton;
    ImageButton preferencesImageButton;

    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> contents = new ArrayList<String>();
    ArrayList<String> timestamps = new ArrayList<String>();
    ConstraintLayout mainLayout;
    InputMethodManager imm;
    Preferences preferences;

    public class Preferences {
        String style = "sans-serif";
        String color = "black";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (ConstraintLayout) findViewById(R.id.MainLayout);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        root = Clorem.getInstance(this, "noteDB").getDatabase();
        settings = root.node("settings");
        notes = root.node("notes");
        Node test = root.node("test");
        test.put("random", "value").commit();

        preferences = getPreferences(settings);


        notesListView = (ListView) findViewById(R.id.notesListView);

        noteEditText = (EditText) findViewById(R.id.noteEditText);
        titleEditText = (EditText) findViewById(R.id.titleEditText);

        noteEditText.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        titleEditText.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        noteEditText.setTextColor(Color.parseColor(preferences.color));
        titleEditText.setTextColor(Color.parseColor(preferences.color));

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        submitButton.setTextColor(Color.parseColor(preferences.color));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String content = noteEditText.getText().toString();

                SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                Date currentTime = new Date();
                String timestamp = format.format(currentTime);

                if (title.length() > 0 && content.length() > 0) {
                    titles.add(title);
                    contents.add(content);
                    timestamps.add(timestamp);

                    Node note = notes.node(title);
                    note
                            .put("content", content)
                            .put("time", timestamp)
                            .commit();
                    titleEditText.setText("");
                    noteEditText.setText("");

                    //hide keyboard
                    imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                } else {
                    Toast.makeText(getApplicationContext(), "title or content empty", Toast.LENGTH_SHORT).show();
                }

                loadNotes(root, preferences);
            }
        });

        preferencesImageButton = (ImageButton) findViewById(R.id.preferencesImageButton);

        preferencesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), SettingsActivity.class);

                //passing information to new activity
                startIntent.putExtra("message", "Hello World!");

                startActivity(startIntent);
            }
        });

        loadNotes(root, preferences);

    }



    private void loadNotes(Node root, Preferences preferences) {
        Node notes = root.node("notes");
        ArrayList<String> foundNotes;
        foundNotes = notes.getChildren();

        if (!titles.isEmpty()) titles.clear();
        if (!contents.isEmpty()) contents.clear();
        if (foundNotes.isEmpty()) return;

        for(String noteName : foundNotes) {
            Node note = notes.node(noteName);

            titles.add(noteName);
            contents.add(note.getString("content", "default note"));
            timestamps.add(note.getString("time", "default time"));
        }

        NoteAdapter noteAdapter = new NoteAdapter(this, titles, contents, timestamps, preferences);
        notesListView.setAdapter(noteAdapter);
    }

    private Preferences getPreferences(Node settings) {
        Preferences preferences = new Preferences();

        String style = settings.getString("style", "sans-serif");
        String color = settings.getString("color", "black");

        preferences.style = style;
        preferences.color = color;

        return preferences;
    }

    @Override
    protected void onResume() {
        super.onResume();
        root = Clorem.getInstance(this, "noteDB").getDatabase();
        settings = root.node("settings");
        notes = root.node("notes");
        Node test = root.node("test");
        test.put("random", "value").commit();

        preferences = getPreferences(settings);


        notesListView = (ListView) findViewById(R.id.notesListView);

        noteEditText = (EditText) findViewById(R.id.noteEditText);
        titleEditText = (EditText) findViewById(R.id.titleEditText);

        noteEditText.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        titleEditText.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        noteEditText.setTextColor(Color.parseColor(preferences.color));
        titleEditText.setTextColor(Color.parseColor(preferences.color));

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setTypeface(Typeface.create(preferences.style, Typeface.NORMAL));
        submitButton.setTextColor(Color.parseColor(preferences.color));

        loadNotes(root, preferences);
    }
}