package com.example.kaunghtetthar.myapplication.DAOs;

import android.util.Log;

import com.example.kaunghtetthar.myapplication.model.parking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaunghtetthar on 5/2/17.
 */

public class OnlineParkingDAO implements IParkingDAO {




    private NetworkDAO networkDAO;

    public OnlineParkingDAO() {
        networkDAO= new NetworkDAO();
    }

    @Override
    public ArrayList<parking> fetchPlants(String searchTerm) {
        return null;
    }

    @Override
    public ArrayList<parking> fetchPlants(int id) throws Exception {


        ArrayList<parking> parkingResults = new ArrayList<>();

        String url = "http://192.168.0.101:8000/json.php";




        // Access a NetworkDAO for low level networking functions.
        NetworkDAO networkDAO = new NetworkDAO();

        //make the request
        String parkingdata = networkDAO.request(url);

        // Pass the data in a JsSON objects.
        JSONArray jsonObject = new JSONArray(parkingdata);


        // iterate over the collections of parkings from json
        for (int i = 0; i < jsonObject.length(); i++) {

            // get our json object from the array.
            JSONObject jsonParking = jsonObject.getJSONObject(i);

            //create a new parking object.
            parking parking = new parking();




            parking.setLatitude(jsonParking.getDouble("lat"));
            parking.setLongitude(jsonParking.getDouble("lng"));
            parking.setFreespaces(jsonParking.getInt("freespace"));
            parking.setVideoStreaming(jsonParking.getString("url"));
            parking.setLocationAddress(jsonParking.getString("name"));

            Log.v("FUN5" , "latlng :" + jsonParking.getDouble("lat"));


            // add the parking object to our results.
            parkingResults.add(parking);
        }


        return parkingResults;
    }

    public List<parking> fetch(parking searchParking) throws Exception {
        String queryURL = "http://192.168.0.101:8000/parking/parkingtext.json";

        String searchTerm = searchParking.getLocationAddress();

        //assemble the entrire query URL.
        final String uri = queryURL + searchTerm;

        // Todo refactor this thread so that we can get results. This small thread is
        // for demonstration only.

        Runnable networkThread = new Runnable() {
            @Override
            public void run() {
                // pass the assembled URI to the network DAO, and get the response.
                try {
                    String request = networkDAO.request(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };


        Thread t = new Thread(networkThread);
        t.start();

        //declare a collection of parkings.
        List<parking> allParkings = new ArrayList<parking>();

        //TODO : parse the return from a String to a collection a parkings.


        return null;
    }


    /**
     *
     * Given a geo location (lat + lng) fetch parking within a specified range.
     * @param longitude
     * @param longitude
     * @return a list of parkings that are in the range from the center point.
     */
    @Override
    public List<parking> fetchParkingsByLocation(double latitude, double longitude, double Range) throws Exception {

        ArrayList<parking> parkingResults = new ArrayList<>();

        String url = "http://192.168.0.101:8000/results.json";




            // Access a NetworkDAO for low level networking functions.
            NetworkDAO networkDAO = new NetworkDAO();

            //make the request
            String parkingdata = networkDAO.request(url);

            // Pass the data in a JsSON objects.
            JSONArray jsonObject = new JSONArray(parkingdata);


            // iterate over the collections of parkings from json
            for (int i = 0; i < jsonObject.length(); i++) {

                // get our json object from the array.
                JSONObject jsonParking = jsonObject.getJSONObject(i);

                //create a new parking object.
                parking parking = new parking();




                    parking.setLatitude(jsonParking.getDouble("lat"));
                    parking.setLongitude(jsonParking.getDouble("long"));
                    parking.setFreespaces(jsonParking.getInt("freespace"));
                    parking.setVideoStreaming(jsonParking.getString("url"));
                    parking.setLocationAddress(jsonParking.getString("name"));

                    Log.v("FUN5" , "latlng :" + jsonParking.getDouble("lat"));


                // add the parking object to our results.
                parkingResults.add(parking);
            }





        return parkingResults;
    }

}
