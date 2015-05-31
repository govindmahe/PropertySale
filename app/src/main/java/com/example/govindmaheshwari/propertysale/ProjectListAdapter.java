package com.example.govindmaheshwari.propertysale;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectListAdapter extends ArrayAdapter{
    ArrayList al;
    public ProjectListAdapter(Context context, ArrayList users) {
        super(context, 0, users);
        al=users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView dishName = (TextView) convertView.findViewById(R.id.dishName);

        // Populate the data into the template view using the data object
        dishName.setText(al.get(position).toString());

        // Return the completed view to render on screen
        return convertView;
    }
}