package com.jammuwalastore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.jammuwalastore.model.City;
import com.jammuwalastore.model.Country;
import com.jammuwalastore.model.State;

import java.util.ArrayList;

public class CountryAdapter<T> extends ArrayAdapter<T> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<T> values;

    public CountryAdapter(Context context, int textViewResourceId,
                          ArrayList<T> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public T getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        if (values.get(position) instanceof Country) {
            Country item = (Country) values.get(position);
            label.setText(item.getName());
        }
        if (values.get(position) instanceof State) {
            State item = (State) values.get(position);
            label.setText(item.getName());
        }
        if (values.get(position) instanceof City) {
            City item = (City) values.get(position);
            label.setText(item.getName());
        }

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        if (values.get(position) instanceof Country) {
            Country item = (Country) values.get(position);
            label.setText(item.getName());
        }
        if (values.get(position) instanceof State) {
            State item = (State) values.get(position);
            label.setText(item.getName());
        }
        if (values.get(position) instanceof City) {
            City item = (City) values.get(position);
            label.setText(item.getName());
        }


        return label;
    }
}
