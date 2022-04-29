package com.example.noteapp;

import static android.content.Context.UI_MODE_SERVICE;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.clorem.db.Clorem;
import com.clorem.db.Node;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<String> titles;
    ArrayList<String> contents;
    ArrayList<String> timestamps;
    Context appContext;
    MainActivity.Preferences settings;

    public NoteAdapter(Context c, ArrayList<String> t, ArrayList<String> n, ArrayList<String> s, MainActivity.Preferences preferences) {
        titles = t;
        contents = n;
        timestamps = s;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        appContext = c;
        settings = preferences;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return titles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.my_listview_detail, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) v.findViewById(R.id.contentTextView);
        TextView timeStampTextView = (TextView) v.findViewById(R.id.timeTextView);
        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);

        deleteButton.setTypeface(Typeface.create(settings.style, Typeface.NORMAL));
        deleteButton.setTextColor(Color.parseColor(settings.color));

        deleteButton.setTag(i);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = (int)deleteButton.getTag();
                String title = titles.get(index);

                Node root = Clorem.getInstance(appContext, "noteDB").getDatabase();
                Node notes = root.node("notes");

                //delete note
                Node nodeToDelete = notes.node(title);
                nodeToDelete.delete();
                root.commit();

                //remove from listviews dataset
                titles.remove(index);
                contents.remove(index);
                timestamps.remove(index);

                notifyDataSetChanged();
            }
        });

        String title = titles.get(i);
        String content = contents.get(i);
        String timestamp = timestamps.get(i);

        titleTextView.setText(title);
        titleTextView.setTypeface(Typeface.create(settings.style, Typeface.NORMAL));
        titleTextView.setTextColor(Color.parseColor(settings.color));

        contentTextView.setText(content);
        contentTextView.setTypeface(Typeface.create(settings.style, Typeface.NORMAL));
        contentTextView.setTextColor(Color.parseColor(settings.color));

        timeStampTextView.setText(timestamp);
        timeStampTextView.setTypeface(Typeface.create(settings.style, Typeface.NORMAL));
        timeStampTextView.setTextColor(Color.parseColor(settings.color));

        return v;
    }
}
