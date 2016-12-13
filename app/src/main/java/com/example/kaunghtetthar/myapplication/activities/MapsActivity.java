package com.example.kaunghtetthar.myapplication.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
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

import com.example.kaunghtetthar.myapplication.MapsDirection.DirectionFinderListener;
import com.example.kaunghtetthar.myapplication.MapsDirection.Route;
import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.fragments.MainFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, DirectionFinderListener {

    final int PERMISSION_LOCATION = 111;

    private GoogleApiClient mGoogleApiClient;
    private MainFragment mainFragment;
    private Button go;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        mainFragment = (MainFragment)getSupportFragmentManager()
                .findFragmentById(R.id.container_main);

        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_main, mainFragment).commit();
        }

        go = (Button) findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


//    private void sendRequest() {
//        String origin = etOrigin.getText().toString();
//        String destination = etDestination.getText().toString();
//        if (origin.isEmpty()) {
//            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (destination.isEmpty()) {
//            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            new DirectionFinder(this, origin, destination).execute();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }


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
    public void onLocationChanged(Location location) {
        Log.v("DOG", "Long:" + location.getLongitude() + " - Lat:" + location.getLatitude());
        mainFragment.setUserMarker(new LatLng(location.getLatitude(),location.getLongitude()));

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
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

    }
}