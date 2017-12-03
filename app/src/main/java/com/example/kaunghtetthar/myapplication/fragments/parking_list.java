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
import java.util.List;


public class parking_list extends Fragment {

    private IParkingDAO parkingDAO;
    private ArrayList<parking> locations;
    private parkingAdapter adapter;
    private Handler handler;
    private parking_list mListFragment;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int id = 0;
    private static String url;



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
        Bundle bundle= getArguments();

            id = bundle.getInt("url");
            url = "http://kaunghtet912.kcnloveanime.com/freespacejson.php?id=" + id;
            Log.v("TAG8", "id +  :"  + id);



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





    private class DoBackgroundTask extends AsyncTask<Integer, Void, List<parking>> {


                ProgressDialog myPd_bar;
                String url = parking_list.url;

                     @Override
                     protected void onPreExecute() {
//                           recyclerView.setAdapter(null);
                         // Access a NetworkDAO for low level networking functions.

                         super.onPreExecute();
                     }

                @Override
                protected List<parking> doInBackground(Integer... integers) {


                    locations.clear();
                    // Access a NetworkDAO for low level networking functions.
                    NetworkDAO networkDAO = new NetworkDAO();

                    try {

                        //make the request
                        String parkingdataAll = networkDAO.request(url);
                        String parkingdata = parkingdataAll.replace("<html>\n</html>","");


                        Log.v("TAG9", "id +  :"  + url);

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
                            parking.setParkingid(jsonParking.getInt("id"));

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
                protected void onPostExecute(List<parking> aVoid) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new DoBackgroundTask().execute();

                        }
                    },3000);

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




