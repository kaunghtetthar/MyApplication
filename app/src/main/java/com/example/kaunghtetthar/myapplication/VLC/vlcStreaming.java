package com.example.kaunghtetthar.myapplication.VLC;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.kaunghtetthar.myapplication.DAOs.NetworkDAO;
import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.activities.MapsActivity;
import com.example.kaunghtetthar.myapplication.adapters.parkingAdapter;
import com.example.kaunghtetthar.myapplication.fragments.parking_list;
import com.example.kaunghtetthar.myapplication.model.parking;

import org.json.JSONArray;
import org.json.JSONObject;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class vlcStreaming extends Activity implements IVLCVout.OnNewVideoLayoutListener {

    private TextView freespace;
    private TextView url;

    private Surface mSurface = null;

    private String mMediaUrl;
    private String k;
    private String freespace1;
    private int id;

    private ArrayList<parking> locations;
    private parkingAdapter adapter;
    private Handler handler;
    private parking_list mListFragment;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    private static final boolean USE_SURFACE_VIEW = true;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String TAG = "JavaActivity";
    private static final String SAMPLE_URL = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_640x360.m4v";
    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FIT_SCREEN = 1;
    private static final int SURFACE_FILL = 2;
    private static final int SURFACE_16_9 = 3;
    private static final int SURFACE_4_3 = 4;
    private static final int SURFACE_ORIGINAL = 5;
    private static int CURRENT_SIZE = SURFACE_BEST_FIT;


    private FrameLayout mVideoSurfaceFrame = null;
    private Bitmap mImageCapture = null;
    private SurfaceView mVideoSurface = null;
    private SurfaceView mSubtitlesSurface = null;
    private TextureView mVideoTexture = null;
    private View mVideoView = null;

    private final Handler mHandler = new Handler();
    private View.OnLayoutChangeListener mOnLayoutChangeListener = null;

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;


    private int mVideoHeight = 0;
    private int mVideoWidth = 0;
    private int mVideoVisibleHeight = 0;
    private int mVideoVisibleWidth = 0;

    private int mVideoSarNum = 0;
    private int mVideoSarDen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "VideoVLC -- onCreate -- START ------------");
        setContentView(R.layout.activity_vlc_test);

        freespace = (TextView) findViewById(R.id.free_space);
        freespace.setText(getIntent().getExtras().getString("freespace"));

        url = (TextView) findViewById(R.id.url);

        freespaceTask fT = new freespaceTask();
        fT.execute(0);

        locations = new ArrayList<>();
        freespace1 = getIntent().getExtras().getString("freespace");

        final ArrayList<String> args = new ArrayList<>();
        args.add("-vvv");
        mLibVLC = new LibVLC(this,args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        id = getIntent().getExtras().getInt("id");
        mMediaUrl = getIntent().getExtras().getString("url");

        play();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(vlcStreaming.this, MapsActivity.class);
                intent.putExtra("url", mMediaUrl);
                intent.putExtra("freespace", freespace1);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        },600000);


//

    }

//    public static void saveFrameLayout(FrameLayout frameLayout, String path) {
//        frameLayout.setDrawingCacheEnabled(true);
//        frameLayout.buildDrawingCache();
//        Bitmap cache = frameLayout.getDrawingCache();
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(path);
//            cache.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (Exception e) {
//            // TODO: handle exception
//        } finally {
//            frameLayout.destroyDrawingCache();
//        }
//    }


    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public static String encodeToBase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean compress = immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }



    private void play(){
        mVideoSurfaceFrame = (FrameLayout) findViewById(R.id.video_surface_frame);
        if (USE_SURFACE_VIEW) {
            ViewStub stub = (ViewStub) findViewById(R.id.surface_stub);
            mVideoSurface = (SurfaceView) stub.inflate();
            if (ENABLE_SUBTITLES) {
                stub = (ViewStub) findViewById(R.id.subtitles_surface_stub);
                mSubtitlesSurface = (SurfaceView) stub.inflate();
                mSubtitlesSurface.setZOrderMediaOverlay(true);
                mSubtitlesSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            }
            mVideoView = mVideoSurface;
        }
        else
        {
            ViewStub stub = (ViewStub) findViewById(R.id.texture_stub);
            mVideoTexture = (TextureView) stub.inflate();
            mVideoView = mVideoTexture;
        }


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
        if (mVideoSurface != null) {
            vlcVout.setVideoView(mVideoSurface);
            if (mSubtitlesSurface != null)
                vlcVout.setSubtitlesView(mSubtitlesSurface);
        }
        else
            vlcVout.setVideoView(mVideoTexture);
            vlcVout.attachViews(this);

        Media media = new Media(mLibVLC, Uri.parse(mMediaUrl));
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();




        final Handler mHandler1 = new Handler();

        mHandler1.post(new Runnable() {
                    @Override
                    public void run() {
                        Media media = new Media(mLibVLC, Uri.parse(mMediaUrl));
                        mMediaPlayer.setMedia(media);
                        media.release();
                        mMediaPlayer.play();


                            // Post again 16ms later.
                            mHandler1.postDelayed(this, 10000);

            }
        });


        if (mOnLayoutChangeListener == null) {
            mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
                private final Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        //updateVideoSurfaces();
                    }
                };
                @Override
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                        mHandler.removeCallbacks(mRunnable);
                        mHandler.post(mRunnable);
                    }
                }
            };
        }
        mVideoSurfaceFrame.addOnLayoutChangeListener(mOnLayoutChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mOnLayoutChangeListener != null) {
            mVideoSurfaceFrame.removeOnLayoutChangeListener(mOnLayoutChangeListener);
            mOnLayoutChangeListener = null;
        }

        mMediaPlayer.stop();

        mMediaPlayer.getVLCVout().detachViews();
    }

    private void changeMediaPlayerLayout(int displayW, int displayH) {
        /* Change the video placement using the MediaPlayer API */
        switch (CURRENT_SIZE) {
            case SURFACE_BEST_FIT:
                mMediaPlayer.setAspectRatio(null);
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_FIT_SCREEN:
            case SURFACE_FILL: {
                Media.VideoTrack vtrack = mMediaPlayer.getCurrentVideoTrack();
                if (vtrack == null)
                    return;
                final boolean videoSwapped = vtrack.orientation == Media.VideoTrack.Orientation.LeftBottom
                        || vtrack.orientation == Media.VideoTrack.Orientation.RightTop;
                if (CURRENT_SIZE == SURFACE_FIT_SCREEN) {
                    int videoW = vtrack.width;
                    int videoH = vtrack.height;

                    if (videoSwapped) {
                        int swap = videoW;
                        videoW = videoH;
                        videoH = swap;
                    }
                    if (vtrack.sarNum != vtrack.sarDen)
                        videoW = videoW * vtrack.sarNum / vtrack.sarDen;

                    float ar = videoW / (float) videoH;
                    float dar = displayW / (float) displayH;

                    float scale;
                    if (dar >= ar)
                        scale = displayW / (float) videoW; /* horizontal */
                    else
                        scale = displayH / (float) videoH; /* vertical */
                    mMediaPlayer.setScale(scale);
                    mMediaPlayer.setAspectRatio(null);
                } else {
                    mMediaPlayer.setScale(0);
                    mMediaPlayer.setAspectRatio(!videoSwapped ? ""+displayW+":"+displayH
                            : ""+displayH+":"+displayW);
                }
                break;
            }
            case SURFACE_16_9:
                mMediaPlayer.setAspectRatio("16:9");
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_4_3:
                mMediaPlayer.setAspectRatio("4:3");
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_ORIGINAL:
                mMediaPlayer.setAspectRatio(null);
                mMediaPlayer.setScale(1);
                break;
        }
    }

    private void updateVideoSurfaces() {
        int sw = getWindow().getDecorView().getWidth();
        int sh = getWindow().getDecorView().getHeight();

        // sanity check
        if (sw * sh == 0) {
            Log.e(TAG, "Invalid surface size");
            return;
        }

        mMediaPlayer.getVLCVout().setWindowSize(sw, sh);

        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
        if (mVideoWidth * mVideoHeight == 0) {
            /* Case of OpenGL vouts: handles the placement of the video using MediaPlayer API */
            lp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoView.setLayoutParams(lp);
            lp = mVideoSurfaceFrame.getLayoutParams();
            lp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoSurfaceFrame.setLayoutParams(lp);
            //changeMediaPlayerLayout(sw, sh);
            return;
        }

        if (lp.width == lp.height && lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            /* We handle the placement of the video using Android View LayoutParams */
            mMediaPlayer.setAspectRatio(null);
            mMediaPlayer.setScale(0);
        }

        double dw = sw, dh = sh;
        final boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }

        // compute the aspect ratio
        double ar, vw;
        if (mVideoSarDen == mVideoSarNum) {
            /* No indication about the density, assuming 1:1 */
            vw = mVideoVisibleWidth;
            ar = (double)mVideoVisibleWidth / (double)mVideoVisibleHeight;
        } else {
            /* Use the specified aspect ratio */
            vw = mVideoVisibleWidth * (double)mVideoSarNum / mVideoSarDen;
            ar = vw / mVideoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;

        switch (CURRENT_SIZE) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_FIT_SCREEN:
                if (dar >= ar)
                    dh = dw / ar; /* horizontal */
                else
                    dw = dh * ar; /* vertical */
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoVisibleHeight;
                dw = vw;
                break;
        }

        // set display size
        lp.width  = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
        lp.height = (int) Math.ceil(dh * mVideoHeight / mVideoVisibleHeight);
        mVideoView.setLayoutParams(lp);
        if (mSubtitlesSurface != null)
            mSubtitlesSurface.setLayoutParams(lp);

        // set frame size (crop if necessary)
        lp = mVideoSurfaceFrame.getLayoutParams();
        lp.width = (int) Math.floor(dw);
        lp.height = (int) Math.floor(dh);
        mVideoSurfaceFrame.setLayoutParams(lp);

        mVideoView.invalidate();
        if (mSubtitlesSurface != null)
            mSubtitlesSurface.invalidate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_parkinginfo, menu);
        return true;
    }

    @Override
    public void onNewVideoLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        mVideoWidth = width;
        mVideoHeight = height;
        mVideoVisibleWidth = visibleWidth;
        mVideoVisibleHeight = visibleHeight;
        mVideoSarNum = sarNum;
        mVideoSarDen = sarDen;
//        updateVideoSurfaces();
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
                    mMediaUrl = parking.getVideoStreaming();
                    url.setText(mMediaUrl);
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

