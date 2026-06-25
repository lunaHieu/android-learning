package com.example.lab05_b1_vanhieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {

    // 1. Store the layout ID to use it later
    private int layoutId;

    // 2. Fix constructor parameters and super call
    public PersonAdapter(@NonNull Context context, int resource, @NonNull List<Person> objects) {
        super(context, resource, objects);
        this.layoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // It is standard to use camelCase (createView) instead of PascalCase (CreateView)
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    // Helper method to generate the view
    private View createView(int position, View convertView, ViewGroup parent) {
        // 3. Inflate the view if it doesn't exist
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }

        // 4. Bind UI elements
        ImageView imageViewPerson = convertView.findViewById(R.id.imageViewSpinner);
        TextView textViewPerson = convertView.findViewById(R.id.textViewSpinner);

        // 5. Get data for this position
        Person person = getItem(position);

        if (person != null) {
            // Ensure your Person class has these getter methods!
            // In MainActivity you used getNamePerson(), here you used getName().
            // Please verify which one is correct in your Person.java.

            imageViewPerson.setImageResource(person.getImage());
            textViewPerson.setText(person.getNamePerson()); // Updated to match your MainActivity logic
        }

        return convertView;
    }
}