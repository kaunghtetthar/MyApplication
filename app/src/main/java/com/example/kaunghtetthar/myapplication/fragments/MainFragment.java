package com.example.kaunghtetthar.myapplication.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.locationroutedirectionmapv2.DirectionsJSONParser;
import com.example.kaunghtetthar.myapplication.model.myapp;
import com.example.kaunghtetthar.myapplication.services.DataService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions userMarker;
    ArrayList<LatLng> markerPoints;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direction_main);

        // Initializing
        markerPoints = new ArrayList<>();

        // Getting Map for the SupportMapFragment

        if (mMap != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            mMap.setMyLocationEnabled(true);

            // Setting onclick event listener for the map
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Already two locations
                    if(markerPoints.size()>1){
                        markerPoints.clear();
                        mMap.clear();
                    }

                    // Adding new item to the ArrayList
                    markerPoints.add(point);

                    // Creating MarkerOptions
                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(point);

                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */
                    if(markerPoints.size()==1){
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }else if(markerPoints.size()==2){
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    // Add new marker to the Google Map Android API V2
                    mMap.addMarker(options);

                    // Checks, whether start and end locations are captured
                    if(markerPoints.size() >= 2){
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }
                }
            });
        }
    }



    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }







    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng AIT = new LatLng(14.078013, 100.614952);
//        mMap.addMarker(new MarkerOptions().position(AIT).title("AIT parking space"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(AIT));
    }

    public void setUserMarker(LatLng latLng) {
        if (userMarker == null) {
            userMarker = new MarkerOptions().position(latLng).title("Current location");
            mMap.addMarker(userMarker);
            Log.v("DOG", "Current location: " + latLng.latitude + " Long: " + latLng.longitude);
        }


        updateMapForZip(12120);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    private void updateMapForZip(int zipcode) {

        ArrayList<myapp> locations = DataService.getInstance().getBootcampLocationWithin10MilesofZip(zipcode);

        for (int x = 0; x < locations.size(); x++) {
            myapp loc = locations.get(x);
            MarkerOptions marker = new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude()));
            marker.title(loc.getLocationTitle());
            marker.snippet(loc.getLocationAddress());
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon));
            mMap.addMarker(marker);
        }

    }

}
