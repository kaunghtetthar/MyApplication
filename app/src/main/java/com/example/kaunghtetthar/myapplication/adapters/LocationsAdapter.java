package com.example.kaunghtetthar.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.holders.LocationsViewHolder;
import com.example.kaunghtetthar.myapplication.model.myapp;

import java.util.ArrayList;

/**
 * Created by kaunghtetthar on 4/20/17.
 */

public class LocationsAdapter extends RecyclerView.Adapter<LocationsViewHolder> {

    private ArrayList<myapp> locations;


    public LocationsAdapter(ArrayList<myapp> locations) {
        this.locations = locations;

    }

    @Override
    public void onBindViewHolder(LocationsViewHolder holder, int position) {
        final  myapp location = locations.get(position);
        holder.updateUI(location);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

    });
    }

    @Override
    public int getItemCount() {
        return locations.size();


    }

    @Override
    public LocationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_info, parent, false);
        return new LocationsViewHolder(card);
    }
}
