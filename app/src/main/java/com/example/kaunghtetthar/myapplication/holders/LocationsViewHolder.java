package com.example.kaunghtetthar.myapplication.holders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.VLC.vlcActivity;
import com.example.kaunghtetthar.myapplication.activities.MapsActivity;
import com.example.kaunghtetthar.myapplication.model.myapp;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kaunghtetthar on 4/20/17.
 */

public class LocationsViewHolder extends RecyclerView.ViewHolder {


    private ImageView locationImage;
    private TextView locationTitle;
    private TextView locationAddress;
    private TextView freespaces;
    private Button videostreaming;
    private Button godriver;
    ProgressDialog pDialog;
    VideoView videoview;
    private Context context;


    public LocationsViewHolder(View itemView) {
        super(itemView);

        locationImage = (ImageView) itemView.findViewById(R.id.location_img);
        locationTitle = (TextView) itemView.findViewById(R.id.location_title);
        locationAddress = (TextView) itemView.findViewById(R.id.location_address);
        freespaces = (TextView) itemView.findViewById(R.id.free_space);
        videostreaming = (Button) itemView.findViewById(R.id.watch_stream);
        godriver = (Button) itemView.findViewById(R.id.go_drive);
        videoview = (VideoView) itemView.findViewById(R.id.myVideo);
        context = itemView.getContext();

    }

    public void updateUI(myapp location) {
        String uri = location.getImgUrl();
        String chgstr = String.valueOf(location.getFreeSpace());
        final String VideoURL = location.getVideoStreaming();

        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
        final LatLng godrive = marker.getPosition();


        int resource = locationImage.getResources()
                .getIdentifier(uri, null, locationImage.getContext().getPackageName());
        locationImage.setImageResource(resource);
        locationTitle.setText(location.getLocationTitle());
        locationAddress.setText(location.getLocationAddress());

        freespaces.setText(location.getFreespacesTitle());
        videostreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To videostreaming activity

                if (v.getId() == videostreaming.getId()) {
                    Intent intent = new Intent(context, vlcActivity.class);
                    intent.putExtra("url", VideoURL);
                    context.startActivity(intent);

                }


            }
        });

        godriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To polyline activity
                    Bundle args = new Bundle();
                    args.putParcelable("LatLng", godrive);
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("go", args);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);








            }
        });



    }

}









