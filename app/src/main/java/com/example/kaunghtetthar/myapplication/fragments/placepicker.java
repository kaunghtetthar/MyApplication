package com.example.kaunghtetthar.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaunghtetthar.myapplication.R;


public class placepicker extends Fragment {

    public placepicker() {
        // Required empty public constructor
    }


    public static placepicker newInstance(String param1, String param2) {
        placepicker fragment = new placepicker();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_placepicker, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
