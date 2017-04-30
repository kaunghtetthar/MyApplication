package com.example.kaunghtetthar.myapplication.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.kaunghtetthar.myapplication.model.myapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaunghtetthar on 4/27/17.
 */

public class ContatsAdapter extends ArrayAdapter{

    List list = new ArrayList();
    private ArrayList<myapp> locations;
    public ContatsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(@Nullable Contacts object) {
        super.add(object);
        list.add(object);
    }



    @Override
    public int getCount() {
        return list.size();
    }



    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    static class ContactHolder {
        int free_space;
    }
}
