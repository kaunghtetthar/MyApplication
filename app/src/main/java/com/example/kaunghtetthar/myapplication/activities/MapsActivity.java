package com.example.kaunghtetthar.myapplication.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaunghtetthar.myapplication.DAOs.IParkingDAO;
import com.example.kaunghtetthar.myapplication.DAOs.NetworkDAO;
import com.example.kaunghtetthar.myapplication.DAOs.OnlineParkingDAO;
import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.fragments.VLCFragment;
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

import org.json.JSONArray;
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
    private Button watch;
    private TextView freespace123;
    private GoogleMap mMap;
    private MarkerOptions userMarker;
    private Marker mSelectedMarker;
    private Marker mSelectedMarker1;

    float mDeclination;
    TextView distance1;
    //    private SensorManager mSensorManager;
    TextView duration1;
    public Button send;
    private parking_list mListFragment;
    private VLCFragment mVLCFragment;
    ListView listview;
    TextView freespace;
    private static final double RANGE = 0.001;
    private IParkingDAO parkingDAO;
    private parking_list ListFragment;
    private parking_list allList;

    double currentlat;
    double currentlng;
    double currentalti;

    public static int y;
    private float go1;
    RecyclerView mRecyclerView;

    int id;
    int goid;
    int freespaces;
    int freespacedata;
    private Marker hidemarker;
    String url;
    String freespace1;
    ArrayList<parking> allparkingresults;


    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager SM;
    private Sensor mySensor;
    private Float xvalue, yvalue, zvalue;
    private RotateAnimation ra;
    private int getSnippet;
    LatLng location1;

    LatLng location2;


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
        Bundle bundle1 = getIntent().getParcelableExtra("location");
        Bundle bundle2 = getIntent().getParcelableExtra("Selected");

        if (bundle1 != null) {
            location1 = bundle1.getParcelable("latlng");
        }

        if (bundle2 != null) {
            location2 = bundle2.getParcelable("LatLng");
            Log.i("TAG5", "latlng" + location2);

        }


        Log.i("TAG4", "latlng" + location1);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_locations);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SupportMapFragment mainFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(sign_in);
        mainFragment.getMapAsync(this);


        // Create our Sensor manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        ListFragment = new parking_list();


        // Initializing
        distance1 = (TextView) findViewById(R.id.distance1);
        duration1 = (TextView) findViewById(R.id.duration1);
        freespace = (TextView) findViewById(R.id.freespace);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();


        ListFragment = (parking_list) getSupportFragmentManager().findFragmentById(R.id.container_locations_list);
        ListFragment = (parking_list) getSupportFragmentManager().findFragmentByTag("TAG");


        if (!isConnected(MapsActivity.this)) buildDialog(MapsActivity.this).show();
        else {
//            Toast.makeText(MapsActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            mainFragment.getMapAsync(this);
        }


//        mVLCFragment = (VLCFragment) getSupportFragmentManager().findFragmentById(R.id.vlc);
//        mVLCFragment = (VLCFragment) getSupportFragmentManager().findFragmentByTag("TAG");
//
//
//
//        if (mVLCFragment == null) {
//            mVLCFragment = VLCFragment.newInstance();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.vlc, mVLCFragment, "TAG").commit();
//
//            ArrayList<parking> parkingResults = new ArrayList<>();
//
//
//            if (mSelectedMarker1 != null) {
//                id = Integer.parseInt(mSelectedMarker1.getId());
//            }
//
//            Bundle bundle3 = new Bundle();
//                bundle3.putString("edttext", "From Activity");
//                bundle3.putString("url", url);
//                bundle3.putString("freespace", freespace1);
//                bundle3.putInt("id", id);
//// set Fragmentclass Arguments;
//                mVLCFragment.setArguments(bundle3);
//
//        }


//        go = (Button) findViewById(R.id.go);
//
//        send = (Button) findViewById(R.id.send);
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle args = new Bundle();
//                LatLng l = mSelectedMarker1.getPosition();
//                args.putParcelable("LatLng", l);
//                Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
//                intent.putExtra("Selected", args);
//                intent.putExtra("lat", mSelectedMarker1.getPosition().latitude);
//                intent.putExtra("lng", mSelectedMarker1.getPosition().longitude);
//                startActivity(intent);
//            }
//        });
//
//        go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                showList();
//                hidelist123();
//              //  hidekeyboard();
//
//            }
//        });

        if (bundle != null) {





            Thread timer = new Thread() {

                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        onMapPolyline();
//                        hidekeyboard();

                    }
                }
            };
            timer.start();
        }


        watch = (Button) findViewById(R.id.watch);

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                intent = new Intent(MapsActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });


        //initialize the parkingDAO.
        parkingDAO = new OnlineParkingDAO();

//        hideList();
    }


    public void onMapPolyline() {


        Bundle bundle = getIntent().getParcelableExtra("go");
        Bundle bundle2 = getIntent().getParcelableExtra("go1");
        LatLng godrive;

        if (bundle2 == null) {
            godrive = bundle.getParcelable("LatLng");
        } else {
            godrive = bundle2.getParcelable("LatLng");
        }


        // Checks, whether start and end locations are captured
        LatLng latLng = userMarker.getPosition();
        LatLng dest = godrive;

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(latLng, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        freespaceTask freespace = new freespaceTask();

        freespace.execute();


    }

    public void onMapPolyline1() {


        double lat = location2.latitude;
        double lng = location2.longitude;


        // Checks, whether start and end locations are captured
        LatLng latLng = userMarker.getPosition();
        LatLng dest = new LatLng(lat, lng);

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(latLng, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        freespaceTask freespaceTask = new freespaceTask();

        freespaceTask.execute();


    }

//
//    // Compare Marker variable instead of MyMarker
//    @Override
////    public boolean equals(Object o) {
////        return o != null && o.equals(mSelectedMarker);
////    }


    @Override
    public boolean onMarkerClick(Marker marker) {

//        if (null != mSelectedMarker) {
//
//            mSelectedMarker = marker;
//
//        }


        if (null != mSelectedMarker1) {


            ListFragment = new parking_list();

            getSnippet = Integer.parseInt(mSelectedMarker1.getSnippet());


            Bundle bundle4 = new Bundle();
            bundle4.putString("edttext", "From Activity");
            bundle4.putInt("url", getSnippet);
            bundle4.putString("freespace", freespace1);
            bundle4.putInt("id", id);
            // set Fragmentclass Arguments;
            ListFragment.setArguments(bundle4);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (ListFragment == null) {

                transaction.add(R.id.container_locations_list, ListFragment, "TAG");

            } else {

                transaction.replace(R.id.container_locations_list, ListFragment, "TAG");

            }
            transaction.addToBackStack(null);
            transaction.commit();

            showlist123();
//            hidekeyboard();


        }
        mSelectedMarker1 = marker;


        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        hidekeyboard();
//        hideList();
//        hidevlc();
        if (null != mSelectedMarker1) {

            hidelist123();
        }

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
        xvalue = (float) Math.round(event.values[0]);
        yvalue = (float) Math.round(event.values[1]);
        zvalue = (float) Math.round(event.values[2]);

        // create a rotation animation (reverse turn degree degrees)

        ra = new RotateAnimation(currentDegree, -xvalue, Animation.RELATIVE_TO_SELF, 0.5f,
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


    private String getDirectionsUrl(LatLng latLng, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + latLng.latitude + "," + latLng.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
//        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.v("TAG7", "Directions url : " + url);

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
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
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            ParserTask parserTask = new ParserTask();
            Double destLat = null, destLng = null;

//            if (mSelectedMarker1 != null) {
//                destLat = mSelectedMarker1.getPosition().latitude;
//                destLng = mSelectedMarker1.getPosition().longitude;
//
//
//            } else if (mSelectedMarker1 == null) {
//                 destLat = Double.valueOf(getIntent().getExtras().getString("lat"));
//                 destLng = Double.valueOf(getIntent().getExtras().getString("lng"));
//            }


            final Location here = new Location("Current");
            here.setLatitude(currentlat);
            here.setLongitude(currentlng);

//            Location dest1 = new Location("Destination");
//            dest1.setLatitude(destLat);
//            dest1.setLongitude(destLng);


            final LatLng curloc = new LatLng(here.getLatitude(), here.getLongitude());
            ;


//            go1 = here.bearingTo(dest1);
//            Log.v("FUN12", "Here to Dest : " + go);
//            mDeclination = go1 - (go1 + mDeclination);
//            Float degrees = (float) Math.round(-mDeclination / 360 + 180);
//            CameraPosition camera = new CameraPosition(curloc, 15, 0, go1);
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));


            if (mSelectedMarker != null) {

                mSelectedMarker.remove();

            }

            final Marker hellomarker;

            hellomarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_icon)).
                    position(curloc).title("Current location : " + curloc.latitude + "," + curloc.longitude));

            hellomarker.setRotation(-90);


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
                            hellomarker.setAnchor(0.5f, 0.5f);
                            hellomarker.setPosition(curloc);
                            CameraPosition camera = new CameraPosition(curloc, 15, 0, xvalue);
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));


                            if (t > 0.0) {
                                // Post again 16ms later.
                                handler.postDelayed(this, 100);
                            }
                        }
                    });
                }
            });


            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
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


            final String finalDuration = duration;
            final String finalDistance = distance;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    distance1.setText("Distance: " + finalDistance);
                    duration1.setText("Duration: " + finalDuration);
                }
            }, 3000);



            // Drawing polyline in the Google Map for the i-th route
//            mMap.addPolyline(lineOptions);

            Polyline polyline = mMap.addPolyline(lineOptions);


            polyline.toString();


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        freespacedata = freespaces;


        // create an instance of the ParkingSearchTask
        ParkingSearchTask pst = new ParkingSearchTask();

        //start the PST thread
        pst.execute(latitude, longitude, RANGE);


        // create an instance of the ParkingSearchTask
        VGLsearch vgLsearch = new VGLsearch();

        //start the PST thread
        vgLsearch.execute(latitude, longitude, RANGE);


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
        timer.scheduleAtFixedRate(updateProfile, 10, 5000);

        //move map camera
        if (location1 != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1, 15.0f));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }


//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));


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

    private void hideallList() {
        getSupportFragmentManager().beginTransaction().hide(allList).commit();
    }

    private void showallList() {
        getSupportFragmentManager().beginTransaction().show(allList).commit();

    }

    private void hidevlc() {
        getSupportFragmentManager().beginTransaction().hide(mVLCFragment).commit();
    }

    private void showvlc() {
        getSupportFragmentManager().beginTransaction().show(mVLCFragment).commit();

    }

    private void showlist123() {

        getSupportFragmentManager().beginTransaction().show(ListFragment).commit();
    }

    private void hidelist123() {

        getSupportFragmentManager().beginTransaction().hide(ListFragment).commit();

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
        protected void onPostExecute(final List<parking> result) {



            for (final parking parking : result) {

                final Marker marker;


                LatLng position = new LatLng(parking.getLatitude(), parking.getLongitude());



                marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)).position(position).
                        title(parking.toString()).
                        snippet(String.valueOf(parking.getParkingid())));

                if (parking.getFreeSpace() == 0) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_redicon));
                }


                marker.isVisible();



                url = parking.getVideoStreaming();
                freespace1 = parking.toString();
                id = parking.getParkingid();
                freespaces = parking.getFreeSpace();

                allparkingresults = new ArrayList<parking>();

                    allparkingresults.addAll(result);
                    Log.v("TAG31", "result:" + allparkingresults.addAll(result));

                // IF connection fail, reload marker again

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        new ParkingSearchTask().execute();
//                    }
//                }, 2000);


//                // IF connection fail, reload marker again
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        boolean results = allparkingresults.addAll(result);

                            if (results) {
                                marker.remove();
                            }

                        new ParkingSearchTask().execute();
                        //add a marker to google map
                    }
                }, 30000);

            }

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    new ParkingSearchTask().execute();
//
//                }
//            },3000);


            super.onPostExecute(result);


        }

        // doInBackground runs in a thread separate from the UI thread,
        // and thus, can perform network operations.
        // We must invoke this by calling a method named execute().

        @Override
        protected List<parking> doInBackground(Double... params) {

            List<parking> parkingResults = new ArrayList<>();

            String url = "http://kcnloveanime.com/kaunghtet912/json.php";


            // Access a NetworkDAO for low level networking functions.
            NetworkDAO networkDAO = new NetworkDAO();


            try {
                //make the request
                String parkingdataAll = networkDAO.request(url);
                String parkingdata = parkingdataAll.replace("<html>\n</html>","");
                Log.v("FUN5", "parking data url :" + parkingdata);


                // Pass the data in a JSON objects.
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
                    parking.setTotalslots(jsonParking.getInt("totalslots"));
                    parking.setParkingid(jsonParking.getInt("id"));
                    Log.v("FUN5", "latlng :" + jsonParking.getDouble("lat"));


                    // add the parking object to our results.
                    parkingResults.add(parking);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return parkingResults;
        }


    }

    private class freespaceTask extends AsyncTask<Integer, Void, List<parking>> {

        // onPostExecute runs in the main/UI thread, and thus,
        // has access to UI objects

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<parking> doInBackground(Integer... params) {

            ArrayList<parking> parkingResults = new ArrayList<>();
            goid = getIntent().getExtras().getInt("id");
            Log.v("id", "no." + goid);
            int goid1 = 0;
            goid1 = getIntent().getExtras().getInt("id1");
            Log.v("id", "2nd" + goid1);

            String url1 = null;

            if (goid1 == 0) {
                 url1 = "http://kaunghtet912.kcnloveanime.com/freespacejson.php?id=" + goid;
            } else {
                 url1 = "http://kaunghtet912.kcnloveanime.com/freespacejson.php?id=" + goid1;

            }

//                locations.clear();
            // Access a NetworkDAO for low level networking functions.
            NetworkDAO networkDAO = new NetworkDAO();

            try {

                

                //make the request
                String parkingdataAll = networkDAO.request(url1);
                String parkingdata = parkingdataAll.replace("<html>\n</html>","");


                // Pass the data in a JsSON objects.
                JSONArray jsonObject = new JSONArray(parkingdata);

//                    JSONObject jsonObject1 = new JSONObject("parking");


                for (int i = 0; i < jsonObject.length(); i++) {

                    // get our json object from the array.
                    JSONObject jsonParking = jsonObject.getJSONObject(i);

                    Log.v("FUN10", "freespace :" + jsonParking);
                    //create a new parking object.
                    parking parking = new parking();

                    parking.setLatitude(jsonParking.getDouble("lat"));
                    parking.setLongitude(jsonParking.getDouble("long"));
                    parking.setFreespaces(jsonParking.getInt("freespace"));
                    parking.setTotalslots(jsonParking.getInt("totalslots"));
                    parking.setFreespacesTitle("freespaces :");
                    parking.setTotalslotsTitle("totalslots :");

                    Log.v("FUN5", "freespace :" + jsonParking.getInt("freespace"));
                    parking.setVideoStreaming(jsonParking.getString("url"));
                    parking.setLocationAddress(jsonParking.getString("name"));

                    Log.v("FUN5", "latlng :" + jsonParking.getDouble("lat"));

                    // add the parking object to our results.
                    parkingResults.add(parking);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return parkingResults;
        }


        double mindist;
        int pos=0;

        @Override
        protected void onPostExecute(final List<parking> result) {

            for (final parking parking : result) {
                final ProgressDialog dialog;
                final AlertDialog.Builder builder2=new AlertDialog.Builder(MapsActivity.this);
                final AlertDialog ad = builder2.create();




                if (parking.getFreeSpace() == 0) {

                        builder2.setMessage("parking lots are not available in this time. If you want to go another nearest parking space, Click Go!");
                        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                ad.cancel();
                            }

                        });

                        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            lauchintent();
                            }


                        });

                        builder2.create();
                        builder2.show();


                    } else {
                    ad.cancel();
                }







                freespace.setText(parking.toString());
                Log.v("FUN9", "id :" + id);
                Log.v("FUN9", "id :" + parking.toString());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new freespaceTask().execute();
                }
            }, 5000);


            super.onPostExecute(result);
        }

        private void lauchintent()
        {

            for (int i = 0; i < allparkingresults.size(); i++) {

                float[] distance = new float[1];
                Location.distanceBetween(currentlat, currentlng, allparkingresults.get(i).getLatitude(), allparkingresults.get(i).getLongitude(), distance);
                if (i == 0) mindist = distance[0];
                else if (mindist > distance[0]) {
                    mindist = distance[0];
                    pos = i;
                }
            }

            double longitude = allparkingresults.get(pos).getLongitude();
            double latitude = allparkingresults.get(pos).getLatitude();

            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude));
            final LatLng godrive = marker.getPosition();

            //To polyline activity
            Bundle args = new Bundle();
            args.putParcelable("LatLng", godrive);
            Intent intent = getIntent();
            intent.putExtra("go1", args);
            intent.putExtra("lat", latitude);
            intent.putExtra("lng", longitude);
            intent.putExtra("freespace", allparkingresults.get(pos).toString());
            intent.putExtra("id1", allparkingresults.get(pos).getParkingid());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

    }

    public class VGLsearch extends AsyncTask<Double, Integer, List<parking>> {

        // onPostExecute runs in the main/UI thread, and thus,
        // has access to UI objects


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final List<parking> result) {



            for (final parking parking : result) {

                final Marker marker;


                LatLng position = new LatLng(parking.getLatitude(), parking.getLongitude());



                marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)).position(position).
                        title(parking.toString()).
                        snippet(String.valueOf(parking.getParkingid())));

                if (parking.getFreeSpace() == 0) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_redicon));
                }


                marker.isVisible();



                url = parking.getVideoStreaming();
                freespace1 = parking.toString();
                id = parking.getParkingid();
                freespaces = parking.getFreeSpace();

                allparkingresults = new ArrayList<parking>();

                allparkingresults.addAll(result);
                Log.v("TAG31", "result:" + allparkingresults.addAll(result));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        boolean results = allparkingresults.addAll(result);

                        if (results) {
                            marker.remove();
                        }

                        new MapsActivity.freespaceTask().execute();
                        //add a marker to google map
                    }
                }, 30000);

            }


            super.onPostExecute(result);


        }

        // doInBackground runs in a thread separate from the UI thread,
        // and thus, can perform network operations.
        // We must invoke this by calling a method named execute().

        @Override
        protected List<parking> doInBackground(Double... params) {

            List<parking> parkingResults = new ArrayList<>();

            String url = "http://kaunghtet912.kcnloveanime.com/VGLresults.json";


            // Access a NetworkDAO for low level networking functions.
            NetworkDAO networkDAO = new NetworkDAO();


            try {
                //make the request
                String parkingdata = networkDAO.request(url);

                // Pass the data in a JSON objects.
                JSONArray jsonObject = new JSONArray(parkingdata);


                // iterate over the collections of parkings from json
                for (int i = 0; i < jsonObject.length(); i++) {

                    // get our json object from the array.
                    JSONObject jsonParking = jsonObject.getJSONObject(i);

                    //create a new parking object.
                    parking parking = new parking();


                    parking.setLatitude(jsonParking.getDouble("Lat"));
                    parking.setLongitude(jsonParking.getDouble("Long"));
                    parking.setFreespaces(jsonParking.getInt("id"));
                    parking.setVideoStreaming(jsonParking.getString("url"));
                    parking.setLocationAddress(jsonParking.getString("name"));
                    parking.setTotalslots(jsonParking.getInt("id"));
                    parking.setParkingid(jsonParking.getInt("id"));
                    Log.v("FUN5", "latlng :" + jsonParking.getDouble("Lat"));


                    // add the parking object to our results.
                    parkingResults.add(parking);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return parkingResults;
        }


    }
}