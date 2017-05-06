package com.example.kaunghtetthar.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.kaunghtetthar.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kaunghtetthar on 12/13/16.
 */

public class MainFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions userMarker;

    private Button go;

    private parking_list mListFragment;

//    private Button btnFindPath;
//    private List<Marker> originMarkers = new ArrayList<>();
//    private List<Marker> destinationMarkers = new ArrayList<>();
//    private List<Polyline> polylinePaths = new ArrayList<>();
//    private ProgressDialog progressDialog;


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


       // btnFindPath = (Button) findViewById(R.id.btnFindPath);

//        btnFindPath.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendRequest();
//            }
//        });

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.sign_in);
        mapFragment.getMapAsync(this);

        mListFragment = (parking_list)getActivity().getSupportFragmentManager().findFragmentById(R.id.container_locations_list);


        if (mListFragment == null) {
            mListFragment = parking_list.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_locations_list, mListFragment).commit();
        }




        final EditText zipText = (EditText)view.findViewById(R.id.zip_text);
        zipText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {



                    //You should make sure this is a valid zip code
                    String text = zipText.getText().toString();
                    int zip = Integer.parseInt(text);

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(zipText.getWindowToken(), 0);
                    showList();
//                    updateMapForZip(zip);
                    return true;
                }
                return false;
            }
        });

        hideList();
        return view;
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


//        updateMapForZip(12120);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

//    private void updateMapForZip(int zipcode) {
//
//        parkingapp task = (parkingapp) getContext();
//        ArrayList<parking> locations = task.getBootcampLocationWithin10MilesofZip(zipcode);
//
//        for (int x = 0; x < locations.size(); x++) {
//            parking loc = locations.get(x);
//            MarkerOptions marker = new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude()));
//            marker.title(loc.getLocationTitle());
//            marker.snippet(loc.getLocationAddress());
//            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.places_ic_clear));
//            mMap.addMarker(marker);
//        }
//
//    }
//
//    private void sendRequest() {
//        String origin =  mMap.toString();
//        String destination = mMap.toString();
//
//        try {
//            new DirectionFinder(this, origin, destination).execute();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onDirectionFinderStart() {
//        progressDialog = ProgressDialog.show(this, "Please wait.",
//                "Finding direction..!", true);
//
//        if (originMarkers != null) {
//            for (Marker marker : originMarkers) {
//                marker.remove();
//            }
//        }
//
//        if (destinationMarkers != null) {
//            for (Marker marker : destinationMarkers) {
//                marker.remove();
//            }
//        }
//
//        if (polylinePaths != null) {
//            for (Polyline polyline:polylinePaths ) {
//                polyline.remove();
//            }
//        }
//    }
//
//
//
//
//    @Override
//    public void onDirectionFinderSuccess(List<Route> routes) {
//        progressDialog.dismiss();
//        polylinePaths = new ArrayList<>();
//        originMarkers = new ArrayList<>();
//        destinationMarkers = new ArrayList<>();
//
//        for (Route route : routes) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
//            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
//            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
//
//            originMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cast_ic_notification_1))
//                    .title(route.startAddress)
//                    .position(route.startLocation)));
//            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cast_ic_notification_2))
//                    .title(route.endAddress)
//                    .position(route.endLocation)));
//
//            PolylineOptions polylineOptions = new PolylineOptions().
//                    geodesic(true).
//                    color(Color.BLUE).
//                    width(10);
//
//            for (int i = 0; i < route.points.size(); i++)
//                polylineOptions.add(route.points.get(i));
//
//            polylinePaths.add(mMap.addPolyline(polylineOptions));
//        }
//    }



    private void hideList() {
        getActivity().getSupportFragmentManager().beginTransaction().hide(mListFragment).commit();
    }

    private void showList() {
        getActivity().getSupportFragmentManager().beginTransaction().show(mListFragment).commit();

    }

}
