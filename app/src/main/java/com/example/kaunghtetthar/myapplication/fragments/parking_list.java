package com.example.kaunghtetthar.myapplication.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaunghtetthar.myapplication.DAOs.IParkingDAO;
import com.example.kaunghtetthar.myapplication.DAOs.NetworkDAO;
import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.activities.MapsActivity;
import com.example.kaunghtetthar.myapplication.adapters.parkingAdapter;
import com.example.kaunghtetthar.myapplication.model.parking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class parking_list extends Fragment {

    private IParkingDAO parkingDAO;
    private ArrayList<parking> locations;
    private parkingAdapter adapter;
    private Handler handler;
    private parking_list mListFragment;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    public parking_list() {
        // Required empty public constructor
    }

    public static parking_list newInstance() {
        parking_list fragment = new parking_list();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_parking_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_locations);
        recyclerView.setHasFixedSize(true);


        new DoBackgroundTask().execute();





        MapsActivity activity = (MapsActivity) getActivity();



        locations = new ArrayList<>();
        adapter = new parkingAdapter(locations, parking_list.this);


//        mListFragment = (parking_list) getChildFragmentManager().findFragmentByTag("FragToRefresh");
//
//
//       if (mListFragment != null) {
//
//            mListFragment.refreshData(locations);
//        }


        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return view;

    }



    ProgressDialog progess;


    private class DoBackgroundTask extends AsyncTask<Integer, Void, Void> {



                     @Override
                     protected void onPreExecute() {
//                           recyclerView.setAdapter(null);
                         super.onPreExecute();
                     }

                @Override
                protected Void doInBackground(Integer... integers) {

                    String url = "http://192.168.0.101:8000/results.json";

                    locations.clear();
                    // Access a NetworkDAO for low level networking functions.
                    NetworkDAO networkDAO = new NetworkDAO();

                    try {

                        //make the request
                        String parkingdata = networkDAO.request(url);

                        // Pass the data in a JsSON objects.
                        JSONArray jsonObject = new JSONArray(parkingdata);
                        Log.v("FUN5", "freespace :" + jsonObject);

                        // iterate over the collections of parkings from json
                        for (int i = 0; i < jsonObject.length(); i++) {

                            // get our json object from the array.
                            JSONObject jsonParking = jsonObject.getJSONObject(i);

                            //create a new parking object.
                            parking parking = new parking();


                            parking.setLatitude(jsonParking.getDouble("lat"));
                            parking.setLongitude(jsonParking.getDouble("long"));
                            parking.setFreespaces(jsonParking.getInt("freespace"));
                            parking.setTotalslots(jsonParking.getInt("totalslots"));
                            parking.setParkingid(jsonParking.getInt("id"));
                            parking.setFreespacesTitle("freespaces :");
                            parking.setTotalslotsTitle("Totalslots :");

                            Log.v("FUN5", "freespace :" + jsonParking.getInt("freespace"));
                            parking.setVideoStreaming(jsonParking.getString("url"));
                            parking.setLocationAddress(jsonParking.getString("name"));

                            Log.v("FUN5", "latlng :" + jsonParking.getDouble("lat"));

                            // add the parking object to our results.
                            locations.add(parking);


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    return null;
                }




                @Override
                protected void onPostExecute(Void aVoid) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new DoBackgroundTask().execute();
                        }
                    },5000);

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    super.onPostExecute(aVoid);


                }


    };


        }



//         new Handler().postDelayed(new Runnable() {
//        public void run() {
//        new NetworkDAO();
//        }
//        }, 10000);



//    public void callAsynchronousTask() {
//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//        TimerTask doAsynchronousTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            PerformBackgroundTask performBackgroundTask = new PerformBackgroundTask();
//                            // PerformBackgroundTask this class is the class that extends AsynchTask
//                            performBackgroundTask.execute();
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                        }
//                    }
//                });
//            }
//        };
//        timer.schedule(doAsynchronousTask, 0, 50000); //execute in every 50000 ms
//    }




