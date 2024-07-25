package com.example.forwardme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.forwardme.Data.Country;
import com.example.forwardme.R;

import java.util.List;

public class CountryCodeAdapter extends ArrayAdapter<Country> {
    private Context context;
    private List<Country> countries;

    public CountryCodeAdapter(Context context, List<Country> countries) {
        super(context, 0, countries);
        this.context = context;
        this.countries = countries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.country_code_item, parent, false);
        }

        Country country = countries.get(position);
        ImageView flagImageView = convertView.findViewById(R.id.flagImageView);
        TextView countryNameTextView = convertView.findViewById(R.id.countryNameTextView);

        flagImageView.setImageResource(country.getFlagResource());
        countryNameTextView.setText(country.toString());

        return convertView;
    }
}

