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
        list.add(new myapp(14.078040f, 100.614946f, "free spaces : " , 3, "On the Campus : " ,  "AIT car parking", "car_icon" , "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
        list.add(new myapp(14.080393f, 100.612730f, "free spaces : " , 4, "On the Campus : " ,  "CSIM car parking", "car_icon", "http://techslides.com/demos/sample-videos/small.mp4"));
        list.add(new myapp(14.078857f, 100.611335f, "free soaces : " , 0,"On the Campus : " ,  "AIT Library parking", "car_icon", "http://techslides.com/demos/sample-videos/small.mp4"));

        return list;
    }
}
