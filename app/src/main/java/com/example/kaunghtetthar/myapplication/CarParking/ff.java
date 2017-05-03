package com.example.kaunghtetthar.myapplication.CarParking;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kaunghtetthar.myapplication.model.parking;
import com.example.kaunghtetthar.myapplication.services.parkingdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kaunghtetthar on 4/27/17.
 */

public class ff {

    private Context context1;
    int list1;
    private static parkingdata mInstance;
    private RequestQueue mRequestQueue ;




    public static  parkingdata getInstance(Context context) {

        if(mInstance != null) {
            mInstance = new parkingdata(context);
        }

        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public ff(Context context) {

        context1 = context;
        mRequestQueue = getRequestQueue();



    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context1);
        }
        return mRequestQueue;
    }



    public ArrayList<parking> getBootcampLocationWithin10MilesofZip(int zipcode) {



        //pretending we are downloading data from the server
        final ArrayList<parking> list = new ArrayList<>();

        final String url = "https://maps.googleapis.com/maps/api/geocode/json?address=Oxford%20University,%20uk&sensor=false" ;



        Log.v("FUN2", url.toString());


        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String final1 = response.toString();

                try {
                    JSONObject json = new JSONObject(String.valueOf(response));
                    JSONArray result = json.getJSONArray("results");
                    int i = 0;

                    while (i < result.length()) {
                        JSONObject fun1 = result.getJSONObject(i);

                        JSONObject fun2 = fun1.getJSONObject("geometry").getJSONObject("viewport")
                                .getJSONObject("southwest");

//                        JSONObject fun3 = fun2.getJSONObject("viewport");


                        String fun4 = fun2.getString("lat");



                        int fun5 = fun2.getInt("lat");
                        list1 = fun2.getInt("lat");

                        String fun = fun4.toString();

                        Log.v("FUN1", "id :" + fun);
                        i++;
                    }


                    Log.v("FUN3" , "id :" + list1);


                } catch (JSONException e) {
                    e.printStackTrace();

                } finally {
//
//                    list.add(new parking(14.078040f, 100.614946f, "free spaces : " , list1, "On the Campus : " ,  "AIT car parking", "car_icon" , "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
//                    list.add(new parking(14.080393f, 100.612730f, "free spaces : " , 3, "On the Campus : " ,  "CSIM car parking", "car_icon", "http://techslides.com/demos/sample-videos/small.mp4"));
//                    list.add(new parking(14.078857f, 100.611335f, "free soaces : " , 5,"On the Campus : " ,  "AIT Library parking", "car_icon", "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"));
//                    Log.v("FUN4" , "id :" + list1);
                }



                String list = response.toString();






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("FUN", "Err: " + error.getLocalizedMessage());
            }
        });

        getRequestQueue().add(jsonRequest);


        return list;



    }
}
