package com.example.kaunghtetthar.myapplication.services;

import com.example.kaunghtetthar.myapplication.model.myapp;

import java.util.ArrayList;

/**
 * Created by kaunghtetthar on 12/12/16.
 */
public class DataService {
    private static DataService Instance = new DataService();

    public static DataService getInstance() {
        return Instance;
    }

    private DataService() {

    }

    public ArrayList<myapp> getBootcampLocationWithin10MilesofZip(int zipcode) {
        //pretending we are downloading data from the server
        ArrayList<myapp> list = new ArrayList<>();
        list.add(new myapp(14.078040f, 100.614946f, "On the Campus : " ,  "AIT car parking", "car_icon"));
        list.add(new myapp(14.080393f, 100.612730f, "On the Campus : " ,  "CSIM car parking", "car_icon"));
        return list;
    }
}
