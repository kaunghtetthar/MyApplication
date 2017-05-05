package com.example.kaunghtetthar.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.fragments.parking_list;
import com.example.kaunghtetthar.myapplication.holders.parkingviewholder;
import com.example.kaunghtetthar.myapplication.model.parking;

import java.util.ArrayList;

/**
 * Created by kaunghtetthar on 4/25/17.
 */

public class parkingAdapter extends RecyclerView.Adapter<parkingviewholder> {

    private ArrayList<parking> locations;
    public parking_list mAct;
    // Start with first item selected
    private int focusedItem = 0;




    public parkingAdapter( ArrayList<parking> locations,parking_list parking_list) {
        this.locations = locations;
        this.mAct = parking_list;
        this.notifyDataSetChanged();
    }

    public void clearAdapter() {
        int size = this.locations.size();
        this.locations.clear();
        notifyItemChanged(0,size);
    }

    public void addAdapter(ArrayList<parking> locations) {
        this.locations.addAll(locations);
        this.notifyItemRangeInserted(0, locations.size() - 1);
    }


    @Override
    public void onBindViewHolder(parkingviewholder holder, int position) {
        final parking location = locations.get(position);
        holder.updateUI(location);

        //To avoid reload item for holder
        holder.setIsRecyclable(false);
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

