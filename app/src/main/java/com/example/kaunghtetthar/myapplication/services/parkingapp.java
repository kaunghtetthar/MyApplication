package com.example.kaunghtetthar.myapplication.services;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.kaunghtetthar.myapplication.model.parking;

import java.util.ArrayList;

/**
 * Created by kaunghtetthar on 4/27/17.
 */

public class parkingapp extends Application {

    private static Context context;
    private static RequestQueue mRequestQueue;
    private Context context1;
    private static parkingapp mInstance;

    static int list1;


    @Override
    public void onCreate() {
        super.onCreate();
        parkingapp.context = getApplicationContext();
        mRequestQueue = getRequestQueue();
    }




    public static Context getAppContext() {
        return parkingapp.context;
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(parkingapp.getAppContext());
        }
        return mRequestQueue;
    }







    public static ArrayList<parking> getBootcampLocationWithin10MilesofZip(int zipcode) {

        final ArrayList<parking> list = new ArrayList<>();

        list.add(new parking(14.078040f, 100.614946f, "free spaces : " , list1, "On the Campus : " ,  "AIT car parking", "car_icon" , "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
        list.add(new parking(14.080393f, 100.612730f, "free spaces : " , 3, "On the Campus : " ,  "CSIM car parking", "car_icon", "http://techslides.com/demos/sample-videos/small.mp4"));
        list.add(new parking(14.078857f, 100.611335f, "free soaces : " , 5,"On the Campus : " ,  "AIT Library parking", "car_icon", "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"));




        return list;



    }


}
