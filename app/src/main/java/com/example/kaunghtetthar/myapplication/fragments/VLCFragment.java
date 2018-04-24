package com.example.kaunghtetthar.myapplication.fragments;

import android.support.v4.app.Fragment;


public class VLCFragment extends Fragment  {

//    private static final String TAG = VLCFragment.class.getSimpleName();
//
//    // size of the video
//    private int mVideoHeight;
//    private int mVideoWidth;
//    private int mVideoVisibleHeight;
//    private int mVideoVisibleWidth;
//    private int mSarNum;
//    private int mSarDen;
//
//    private SurfaceView mSurfaceView;
//    private FrameLayout mSurfaceFrame;
//    private SurfaceHolder mSurfaceHolder;
//    private TextView freespace;
//    private Surface mSurface = null;
//
//    private LibVLC mLibVLC;
//
//    private String mMediaUrl;
//    private int id;
//
//
//    public VLCFragment() {
//        // Required empty public constructor
//    }
//
//
//    public static VLCFragment newInstance() {
//        VLCFragment fragment = new VLCFragment();
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_vlc, container, false);
//        // Inflate the layout for this fragment
//
//        mSurfaceView = (SurfaceView) view.findViewById(R.id.player_surface);
//        mSurfaceHolder = mSurfaceView.getHolder();
//
//        freespace = (TextView) view.findViewById(R.id.free_space);
//        freespace.setText(getArguments().getString("freespace"));
//        Log.v("TAG9", "freespace :" + getArguments().getString("freespace"));
//
//        VLCFragment.freespaceTask fT = new VLCFragment.freespaceTask();
//        fT.execute(0);
//
//
//        mSurfaceFrame = (FrameLayout) view.findViewById(R.id.video_surface_frame);
//        mMediaUrl = getArguments().getString("url");
//        id = getArguments().getInt("id");
////        mMediaUrl = "http://192.168.0.101:1234";
//
//        try {
//            mLibVLC = new LibVLC();
//            mLibVLC.setAout(mLibVLC.AOUT_AUDIOTRACK);
//            mLibVLC.setVout(mLibVLC.VOUT_ANDROID_SURFACE);
//            mLibVLC.setHardwareAcceleration(LibVLC.HW_ACCELERATION_FULL);
//
//            mLibVLC.init(getContext());
//        } catch (LibVlcException e){
//            Log.e(TAG, e.toString());
//        }
//
//
//        mSurface = mSurfaceHolder.getSurface();
//
//        mLibVLC.attachSurface(mSurface, VLCFragment.this);
//        if(mMediaUrl != null) {
//            mLibVLC.playMRL(mMediaUrl);
//        }
//        return  view;
//    }
//
//    @Override
//    public void onDestroyOptionsMenu() {
//        super.onDestroyOptionsMenu();
//        mLibVLC.stop();
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mLibVLC.stop();
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void eventHardwareAccelerationError() {
//        Log.e(TAG, "eventHardwareAccelerationError()!");
//        return;
//    }
//
//    @Override
//    public void setSurfaceLayout(final int width, final int height, int visible_width, int visible_height, final int sar_num, int sar_den){
//        Log.d(TAG, "setSurfaceSize -- START");
//        if (width * height == 0)
//            return;
//
//        // store video size
//        mVideoHeight = height;
//        mVideoWidth = width;
//        mVideoVisibleHeight = visible_height;
//        mVideoVisibleWidth = visible_width;
//        mSarNum = sar_num;
//        mSarDen = sar_den;
//
//        Log.d(TAG, "setSurfaceSize -- mMediaUrl: " + mMediaUrl + " mVideoHeight: " + mVideoHeight + " mVideoWidth: " + mVideoWidth + " mVideoVisibleHeight: " + mVideoVisibleHeight + " mVideoVisibleWidth: " + mVideoVisibleWidth + " mSarNum: " + mSarNum + " mSarDen: " + mSarDen);
//    }
//
//    @Override
//    public int configureSurface(android.view.Surface surface, int i, int i1, int i2){
//        return -1;
//    }
//
//    public class freespaceTask extends AsyncTask<Integer, Void, List<parking>> {
//
//        // onPostExecute runs in the main/UI thread, and thus,
//        // has access to UI objects
//
//        @Override
//        protected void onPreExecute() {
//            freespace.setText(null);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected ArrayList<parking> doInBackground(Integer... params) {
//
//            ArrayList<parking> parkingResults = new ArrayList<>();
//            String url = "http://192.168.0.104:8000/freespacejson.php?id=" + 2;
//            Log.v("TAG9", "VLCFragment" + url);
//
////                locations.clear();
//            // Access a NetworkDAO for low level networking functions.
//            NetworkDAO networkDAO = new NetworkDAO();
//
//            try {
//
//                //make the request
//                String parkingdata = networkDAO.request(url);
//
//                // Pass the data in a JsSON objects.
//                JSONArray jsonObject = new JSONArray(parkingdata);
//
////                    JSONObject jsonObject1 = new JSONObject("parking");
//
//
//                for (int i = 0; i < jsonObject.length(); i++) {
//
//                    // get our json object from the array.
//                    JSONObject jsonParking = jsonObject.getJSONObject(i);
//
//                    Log.v("FUN10", "freespace :" + jsonParking);
//                    //create a new parking object.
//                    parking parking = new parking();
//
//                    parking.setLatitude(jsonParking.getDouble("lat"));
//                    parking.setLongitude(jsonParking.getDouble("long"));
//                    parking.setFreespaces(jsonParking.getInt("freespace"));
//                    parking.setTotalslots(jsonParking.getInt("totalslots"));
//                    parking.setFreespacesTitle("freespaces :");
//                    parking.setTotalslotsTitle("totalslots :");
//
//                    Log.v("FUN5", "freespace :" + jsonParking.getInt("freespace"));
//                    parking.setVideoStreaming(jsonParking.getString("url"));
//                    parking.setLocationAddress(jsonParking.getString("name"));
//
//                    Log.v("FUN5", "latlng :" + jsonParking.getDouble("lat"));
//
//                    // add the parking object to our results.
//                    parkingResults.add(parking);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return parkingResults;
//        }
//
//        @Override
//        protected void onPostExecute(List<parking> result) {
//            for(parking parking : result) {
//                freespace.setText(parking.toString());
//                Log.v("FUN9", "id :" + id);
//            }
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    new VLCFragment.freespaceTask().execute();
//                }
//            },10000);
//
//
//            super.onPostExecute(result);
//        }
//
//    }
}
