package com.example.kaunghtetthar.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.holders.parkingviewholder;
import com.example.kaunghtetthar.myapplication.model.parking;

import java.util.ArrayList;

/**
 * Created by kaunghtetthar on 4/25/17.
 */

public class parkingAdapter extends RecyclerView.Adapter<parkingviewholder> {

    private ArrayList<parking> locations;



    public parkingAdapter(ArrayList<parking> locations) {
        this.locations = locations;

    }

    @Override
    public void onBindViewHolder(parkingviewholder holder, int position) {
        final parking location = locations.get(position);
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
    public parkingviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_info, parent, false);
        return new parkingviewholder(card);
    }
}

