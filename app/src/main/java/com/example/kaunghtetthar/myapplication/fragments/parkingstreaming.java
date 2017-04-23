package com.example.kaunghtetthar.myapplication.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.model.myapp;
import com.example.kaunghtetthar.myapplication.services.DataService;

import java.util.ArrayList;


public class parkingstreaming extends Activity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;

    final ArrayList<myapp> locations = DataService.getInstance().getBootcampLocationWithin10MilesofZip(12120);

    final  myapp location = locations.get(0);

    // Insert your Video URL
//    String VideoURL = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
    String VideoURL = location.getVideoStreaming();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the layout from video_main.xml
        setContentView(R.layout.activity_video_streaming);
        // Find your VideoView in your video_main.xml layout
        videoview = (VideoView) findViewById(R.id.myVideo);
        // Execute StreamVideo AsyncTask

        // Create a progressbar
        pDialog = new ProgressDialog(parkingstreaming.this);
        // Set progressbar title
        pDialog.setTitle("Android Video Streaming Tutorial");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    parkingstreaming.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });


    }


}

