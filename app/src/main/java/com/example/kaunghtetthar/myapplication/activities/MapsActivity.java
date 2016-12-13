package com.example.kaunghtetthar.myapplication.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kaunghtetthar.myapplication.MapsDirection.DirectionFinder;
import com.example.kaunghtetthar.myapplication.MapsDirection.DirectionFinderListener;
import com.example.kaunghtetthar.myapplication.MapsDirection.Route;
import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.model.myapp;
import com.example.kaunghtetthar.myapplication.services.DataService;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, DirectionFinderListener {

    final int PERMISSION_LOCATION = 111;

    private GoogleApiClient mGoogleApiClient;
    private MapsActivity mainFragment;
    private Button go;
    private GoogleMap mMap;
    private MarkerOptions userMarker;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();


        SupportMapFragment mainFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mainFragment.getMapAsync(this);

        go = (Button) findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });


    }

    public void setUserMarker(LatLng latLng) {
        if (userMarker == null) {
            userMarker = new MarkerOptions().position(latLng).title("Current location : " + latLng.latitude + "," + latLng.longitude);
            mMap.addMarker(userMarker);
            Log.v("DOG", "Current location: " + latLng.latitude + " Long: " + latLng.longitude);
        }


        updateMapForZip(12120);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void updateMapForZip(int zipcode) {

        ArrayList<myapp> locations = DataService.getInstance().getBootcampLocationWithin10MilesofZip(zipcode);

        for (int x = 0; x < locations.size(); x++) {
            myapp loc = locations.get(x);
            MarkerOptions marker = new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude()));
            marker.title(loc.getLocationTitle());
            marker.snippet(loc.getLocationAddress());
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon));
            mMap.addMarker(marker);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("DOG", "Long:" + location.getLongitude() + " - Lat:" + location.getLatitude());
        setUserMarker(new LatLng(location.getLatitude(), location.getLongitude()));

    }


    private void sendRequest() {
        ArrayList<myapp> locations = DataService.getInstance().getBootcampLocationWithin10MilesofZip(12120);
        myapp loc = locations.get(0);
        String origin = userMarker.toString();
        String destination = locations.get(0).getLocationAddress().toString();

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);

//        LatLng AIT = new LatLng(14.078013, 100.614952);
//        mMap.addMarker(new MarkerOptions().position(AIT).title("AIT parking space"));
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


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.duration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.distance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_media_play))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_media_pause))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}