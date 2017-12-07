package com.example.kaunghtetthar.myapplication.VLC;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.kaunghtetthar.myapplication.DAOs.NetworkDAO;
import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.adapters.parkingAdapter;
import com.example.kaunghtetthar.myapplication.fragments.parking_list;
import com.example.kaunghtetthar.myapplication.model.parking;

import org.json.JSONArray;
import org.json.JSONObject;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;

import java.util.ArrayList;
import java.util.List;


public class vlcStreaming extends Activity implements IVideoPlayer {
    private static final String TAG = vlcStreaming.class.getSimpleName();

    // size of the video
    private int mVideoHeight;
    private int mVideoWidth;
    private int mVideoVisibleHeight;
    private int mVideoVisibleWidth;
    private int mSarNum;
    private int mSarDen;

    private SurfaceView mSurfaceView;
    private FrameLayout mSurfaceFrame;
    private SurfaceHolder mSurfaceHolder;
    private TextView freespace;
    private Surface mSurface = null;

    private LibVLC mLibVLC;

    private String mMediaUrl;
    private int id;

    private ArrayList<parking> locations;
    private parkingAdapter adapter;
    private Handler handler;
    private parking_list mListFragment;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "VideoVLC -- onCreate -- START ------------");
        setContentView(R.layout.activity_vlc_streaming);

        mSurfaceView = (SurfaceView) findViewById(R.id.player_surface);
        mSurfaceHolder = mSurfaceView.getHolder();

        freespace = (TextView) findViewById(R.id.free_space);
        freespace.setText(getIntent().getExtras().getString("freespace"));

        freespaceTask fT = new freespaceTask();
        fT.execute(0);

        locations = new ArrayList<>();


        mSurfaceFrame = (FrameLayout) findViewById(R.id.video_surface_frame);
        mMediaUrl = getIntent().getExtras().getString("url");
        id = getIntent().getExtras().getInt("id");
//        mMediaUrl = "http://192.168.0.101:1234";

        try {
            mLibVLC = new LibVLC();
            mLibVLC.setAout(mLibVLC.AOUT_AUDIOTRACK);
            mLibVLC.setVout(mLibVLC.VOUT_ANDROID_SURFACE);
            mLibVLC.setHardwareAcceleration(LibVLC.HW_ACCELERATION_FULL);

            mLibVLC.init(getApplicationContext());
        } catch (LibVlcException e){
            Log.e(TAG, e.toString());
        }


        mSurface = mSurfaceHolder.getSurface();

        mLibVLC.attachSurface(mSurface, vlcStreaming.this);
        mLibVLC.playMRL(mMediaUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaCodec opaque direct rendering should not be used anymore since there is no surface to attach.
        mLibVLC.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_parkinginfo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void eventHardwareAccelerationError() {
        Log.e(TAG, "eventHardwareAccelerationError()!");
        return;
    }

    @Override
    public void setSurfaceLayout(final int width, final int height, int visible_width, int visible_height, final int sar_num, int sar_den){
        Log.d(TAG, "setSurfaceSize -- START");
        if (width * height == 0)
            return;

        // store video size
        mVideoHeight = height;
        mVideoWidth = width;
        mVideoVisibleHeight = visible_height;
        mVideoVisibleWidth = visible_width;
        mSarNum = sar_num;
        mSarDen = sar_den;

        Log.d(TAG, "setSurfaceSize -- mMediaUrl: " + mMediaUrl + " mVideoHeight: " + mVideoHeight + " mVideoWidth: " + mVideoWidth + " mVideoVisibleHeight: " + mVideoVisibleHeight + " mVideoVisibleWidth: " + mVideoVisibleWidth + " mSarNum: " + mSarNum + " mSarDen: " + mSarDen);
    }

    @Override
    public int configureSurface(android.view.Surface surface, int i, int i1, int i2){
        return -1;
    }

    public class freespaceTask extends AsyncTask<Integer, Void, List<parking>> {

        // onPostExecute runs in the main/UI thread, and thus,
        // has access to UI objects

        @Override
        protected void onPreExecute() {
            freespace.setText(null);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<parking> doInBackground(Integer... params) {

            ArrayList<parking> parkingResults = new ArrayList<>();
                String url = "http://kaunghtet912.kcnloveanime.com/freespacejson.php?id=" + id;

//                locations.clear();
                // Access a NetworkDAO for low level networking functions.
                NetworkDAO networkDAO = new NetworkDAO();

                try {

                    //make the request
                    String parkingdataAll = networkDAO.request(url);
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

        @Override
        protected void onPostExecute(List<parking> result) {
                for(parking parking : result) {
                    freespace.setText(parking.toString());
                    Log.v("FUN9", "id :" + id);
                }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new freespaceTask().execute();
                }
            },5000);


           super.onPostExecute(result);
            }

        }

        // doInBackground runs in a thread separate from the UI thread,
        // and thus, can perform network operations.
        // We must invoke this by calling a method named execute().




    }

