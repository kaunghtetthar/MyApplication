package com.example.kaunghtetthar.myapplication.holders;

import android.view.View;
import android.widget.TextView;

import com.example.kaunghtetthar.myapplication.R;

/**
 * Created by kaunghtetthar on 5/3/17.
 */



public class freespaceviewholder  {

    public TextView freespaces;


    public freespaceviewholder(View v){
        freespaces = (TextView) v.findViewById(R.id.free_space);
    }
}

