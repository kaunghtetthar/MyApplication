package com.example.kaunghtetthar.myapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.activities.MapsActivity;
import com.example.kaunghtetthar.myapplication.adapters.parkingAdapter;


public class parking_list extends Fragment {


    public parking_list() {
        // Required empty public constructor
    }

        public static parking_list newInstance () {
            parking_list fragment = new parking_list();
            return fragment;
        }

        @Override
        public void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){

            View view = inflater.inflate(R.layout.fragment_parking_list, container, false);

            RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_locations);
            recyclerView.setHasFixedSize(true);

            MapsActivity activity = (MapsActivity) getActivity();
            parkingAdapter adapter = new parkingAdapter(activity.getBootcampLocationWithin10MilesofZip(12120));


            recyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            return view;
        }




    }