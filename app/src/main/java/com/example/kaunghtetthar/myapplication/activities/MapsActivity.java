package com.example.kaunghtetthar.myapplication.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaunghtetthar.myapplication.DAOs.IParkingDAO;
import com.example.kaunghtetthar.myapplication.DAOs.OnlineParkingDAO;
import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.fragments.parking_list;
import com.example.kaunghtetthar.myapplication.locationroutedirectionmapv2.DirectionsJSONParser;
import com.example.kaunghtetthar.myapplication.model.parking;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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
import java.util.Timer;
import java.util.TimerTask;

import static com.example.kaunghtetthar.myapplication.R.id.sign_in;


public class MapsActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, SensorEventListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    final int PERMISSION_LOCATION = 111;

    private GoogleApiClient mGoogleApiClient;
    private Button go;
    private GoogleMap mMap;
    private MarkerOptions userMarker;
    private Marker mSelectedMarker;
    float mDeclination;
    TextView distance1;
//    private SensorManager mSensorManager;
    TextView duration1;
    public Button send;
    private parking_list mListFragment;
    ListView listview;
    TextView freespace;
    private static final double RANGE = 0.001;
    private IParkingDAO parkingDAO;

    double currentlat;
    double currentlng;
    double currentalti;

    public static int y;
    private float go1;


    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager SM;
    private Sensor mySensor;
    private Float xvalue, yvalue, zvalue;




    public MapsActivity() {
        // Required empty public constructor
    }

//    public static MapsActivity newInstance() {
//        MapsActivity fragment = new MapsActivity();
//        return fragment;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        Bundle bundle = getIntent().getParcelableExtra("go");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        // Create our Sensor manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        // Initializing
        distance1 = (TextView) findViewById(R.id.distance1);
        duration1 = (TextView) findViewById(R.id.duration1);
        freespace = (TextView) findViewById(R.id.free_space);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mainFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(sign_in);
        mainFragment.getMapAsync(this);

        mListFragment = (parking_list) getSupportFragmentManager().findFragmentById(R.id.container_locations_list);
        mListFragment = (parking_list) getSupportFragmentManager().findFragmentByTag("TAG");

        if (mListFragment == null) {
            mListFragment = parking_list.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_locations_list, mListFragment, "TAG").commit();

        }






        final EditText zipText = (EditText) findViewById(R.id.zip_text);
        zipText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //You should make sure this is a valid zip code
                    String text = zipText.getText().toString();
                    int zip = Integer.parseInt(text);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(zipText.getWindowToken(), 0);
                    showList();
                    return true;
                }
                hidekeyboard();
             return false;
            }

        });




        go = (Button) findViewById(R.id.go);

        send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showList();
                hidekeyboard();

            }
        });

        if (bundle != null) {

            Thread timer = new Thread() {

                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        onMapPolyline();
                        hidekeyboard();

                    }
                }
            };
                timer.start();
            } else {

            }

        //initialize the parkingDAO.
        parkingDAO = new OnlineParkingDAO();

        hideList();
    }




    public void onMapPolyline() {



        Bundle bundle =  getIntent().getParcelableExtra("go");
        LatLng godrive = bundle.getParcelable("LatLng");



        // Checks, whether start and end locations are captured
        LatLng latLng = userMarker.getPosition();
        LatLng dest = godrive;

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(latLng, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);





    }

//
//    // Compare Marker variable instead of MyMarker
//    @Override
////    public boolean equals(Object o) {
////        return o != null && o.equals(mSelectedMarker);
////    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (null != mSelectedMarker) {
            mSelectedMarker = marker;

        }



        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {

        hidekeyboard();
        hideList();


    }




    public void setUserMarker(LatLng latLng) {
        if (userMarker == null) {
            userMarker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_icon)).
            title("Current location : " + latLng.latitude + "," + latLng.longitude);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


            Log.v("DOG", "Current location: " + latLng.latitude + " Long: " + latLng.longitude);
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        xvalue = Float.valueOf(Math.round(event.values[0]));
        yvalue = event.values[1];
        zvalue = event.values[2];

        // create a rotation animation (reverse turn degree degrees)

        final RotateAnimation ra = new RotateAnimation(currentDegree,-xvalue, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // how long the animation will take place
        ra.setDuration(210);


        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class CustomTimerTask extends TimerTask {
        private Context context;
        private Handler mHandler = new Handler();



        // Write Custom Constructor to pass Context
        public CustomTimerTask(Context con) {
            this.context = con;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {


                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            final Handler handler = new Handler();
//                            final long start = SystemClock.uptimeMillis();
//                            final long duration = 3000;
//
//                            final Interpolator interpolator = new BounceInterpolator();
//
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    long elapsed = SystemClock.uptimeMillis() - start;
//                                    float t = Math.max(
//                                            1 - interpolator.getInterpolation((float) elapsed
//                                                    / duration), 0);
//                                    mSelectedMarker.setAnchor( 0.5f, 1f*t);
//
//                                    if (t > 0.0) {
//                                        // Post again 16ms later.
//                                        handler.postDelayed(this, 16);
//                                    }
//                                }
//                            });
                        }
                    });
                }
            }).start();

        }

    }



    private String getDirectionsUrl(LatLng latLng,LatLng dest){

        // Origin of route
        String str_origin = "origin="+latLng.latitude+","+latLng.longitude;

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
    private class DownloadTask extends AsyncTask<String, Void, String>{

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
                Double destLat = Double.valueOf(getIntent().getExtras().getString("lat"));
                Double destLng = Double.valueOf(getIntent().getExtras().getString("lng"));

                final Location here = new Location("Current");
                here.setLatitude(currentlat);
                here.setLongitude(currentlng);

                Location dest1 = new Location("Destination");
                dest1.setLatitude(destLat);
                dest1.setLongitude(destLng);


             final LatLng curloc = new LatLng(here.getLatitude(), here.getLongitude());
                ;


                go1 = here.bearingTo(dest1);
                Log.v("FUN12", "Here to Dest : " + go);
                mDeclination = go1 - (go1 + mDeclination);
                Float degrees = (float) Math.round(-mDeclination / 360 + 180);
                CameraPosition camera = new CameraPosition(curloc, 15, 0, go1);
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));


                if (mSelectedMarker != null) {

                    mSelectedMarker.remove();

                }

                mSelectedMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_icon)).
                        position(curloc).title("Current location : " + curloc.latitude + "," + curloc.longitude));

            mSelectedMarker.setRotation(-90);


//                mSelectedMarker.setRotation(go1);
//               mSelectedMarker.getRotation();
            Handler mHandler = new Handler();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                            final Handler handler = new Handler();
                            final long start = SystemClock.uptimeMillis();
                            final Float duration = Float.POSITIVE_INFINITY;

                            final Interpolator interpolator = new BounceInterpolator();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    long elapsed = SystemClock.uptimeMillis() - start;
                                    float t = Math.max(
                                            1 - interpolator.getInterpolation((float) elapsed
                                                    / duration), 0);
                                    mSelectedMarker.setAnchor( 0.5f, 0.5f );
                                    mSelectedMarker.setPosition(curloc);
                                    CameraPosition camera = new CameraPosition(curloc, 15, 0, xvalue);
                                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));

                                    if (t > 0.0) {
                                        // Post again 16ms later.
                                        handler.postDelayed(this, 16);
                                    }
                                }
                            });
                }
            });




                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

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
                String distance = "";
                String duration = "";

                if (result.size() < 1) {
                    Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        if (j == 0) {    // Get distance from the list
                            distance = (String) point.get("distance");
                            continue;
                        } else if (j == 1) { // Get duration from the list
                            duration = (String) point.get("duration");
                            continue;
                        }

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


                distance1.setText("Distance:" + distance);
                duration1.setText("Duration" + duration);


                // Drawing polyline in the Google Map for the i-th route
//            mMap.addPolyline(lineOptions);

                Polyline polyline = mMap.addPolyline(lineOptions);



                polyline.toString();


            }

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main , menu);
        return true;
    }





    @Override
    public void onLocationChanged(Location location) {
        Log.v("DOG", "Long:" + location.getLongitude() + " - Lat:" + location.getLatitude());
        setUserMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        currentlat = latitude;
        currentlng = longitude;


        // create an instance of the ParkingSearchTask
        ParkingSearchTask pst = new ParkingSearchTask();

        //start the PST thread
        pst.execute(latitude, longitude, RANGE);



        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        GeomagneticField field = new GeomagneticField(
                (float) location.getLatitude(),
                (float) location.getLongitude(),
                (float) location.getAltitude(),
                System.currentTimeMillis()
        );


        mDeclination += field.getDeclination();




        if (mSelectedMarker != null) {

            mSelectedMarker.remove();

        }

            mSelectedMarker = mMap.addMarker(new MarkerOptions().
                    position(new LatLng(location.getLatitude(), location.getLongitude())).title("Current location : " + latLng.latitude + "," + latLng.longitude));

        Timer timer = new Timer();
        TimerTask updateProfile = new CustomTimerTask(MapsActivity.this);
        timer.scheduleAtFixedRate(updateProfile, 10,5000);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);





//        LatLng AIT = new LatLng(14.078013, 100.614952);
//        mMap.addMarker(new MarkerOptions().position(AIT).title("AIT parking space1"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(AIT));

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            Log.v("DOG", "Requesting permissions");
        } else {
            Log.v("DOG", "Starting Location Services from onConnected");
            startLocationServices();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
//
    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        SM.registerListener(this, SM.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);


    }
//
    @Override
    protected void onPause() {
        super.onPause();
        SM.unregisterListener(this);

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationServices();
                    Log.v("DOG", "Permission Granted - starting services");
                } else {
                    //show a dialog saying something, "I can't run your location - you denied permission!"
                    Log.v("DOG", "Permissions not granted");

                }
            }
        }
    }

    public void startLocationServices() {
        Log.v("DOG", "Starting Location Services Called");

        try {
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this);
            Log.v("DOG", "Requesting location updates");
        } catch (SecurityException exception) {
            //Show dialog to user saying we can't get location unless they give app permission
            Log.v("DOG", exception.toString());
        }
    }

    private void hideList() {
        getSupportFragmentManager().beginTransaction().hide(mListFragment).commit();
    }

    private void showList() {
        getSupportFragmentManager().beginTransaction().show(mListFragment).commit();

    }

    private void hidekeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    public class ParkingSearchTask extends AsyncTask<Double, Integer, List<parking>> {

        // onPostExecute runs in the main/UI thread, and thus,
        // has access to UI objects


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<parking> result) {
            for (final parking parking: result) {
                LatLng position = new LatLng(parking.getLatitude(), parking.getLongitude());

                final MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)).position(position).
                        title(parking.toString()).
                        snippet(parking.getLocationAddress());




                final Marker maker = mMap.addMarker(marker);




                // IF connection fail, reload marker again
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (maker != null) {
                            maker.remove();
                        }
                        //add a marker to google map
                        mMap.addMarker(marker);
                    }
                });

            }





        }

        // doInBackground runs in a thread separate from the UI thread,
        // and thus, can perform network operations.
        // We must invoke this by calling a method named execute().

        @Override
        protected List<parking> doInBackground(Double... params) {

            List<parking> parkingsByLocation = new ArrayList<>();
            try {
                parkingsByLocation = parkingDAO.fetchParkingsByLocation(params[0], params[1], params[2]);
            } catch (Exception e) {
                e.printStackTrace();
        }

            return parkingsByLocation;
    }

    }


}