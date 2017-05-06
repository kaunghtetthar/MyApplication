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
import com.example.kaunghtetthar.myapplication.VLC.vlcStreaming;
import com.example.kaunghtetthar.myapplication.activities.MapsActivity;
import com.example.kaunghtetthar.myapplication.model.parking;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kaunghtetthar on 4/25/17.
 */

public class parkingviewholder extends RecyclerView.ViewHolder {

    private ImageView locationImage;
    private TextView locationTitle;
    private TextView locationAddress;
    private TextView freespaces;
    private Button videostreaming;
    private Button godriver;
    ProgressDialog pDialog;
    VideoView videoview;
    private Context context;


    public parkingviewholder(View itemView) {
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

    public void updateUI(final parking location) {
        String uri = location.getImgUrl();
        final String chgstr = String.valueOf(location.getFreeSpace());
        final String VideoURL = location.getVideoStreaming();
        final String freespace = location.getFreespacesTitle();
        final int id = location.getParkingid();
        final String latitude = String.valueOf(location.getLatitude());
        final String longitude = String.valueOf(location.getLongitude());

        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
        final LatLng godrive = marker.getPosition();


        int resource = locationImage.getResources()
                .getIdentifier(uri, null, locationImage.getContext().getPackageName());
        locationImage.setImageResource(resource);
        locationTitle.setText(location.getLocationTitle());
        locationAddress.setText(location.getLocationAddress());

        freespaces.setText(location.toString());
        videostreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To videostreaming activity

                if (v.getId() == videostreaming.getId()) {
                    Intent intent = new Intent(context, vlcStreaming.class);
                    intent.putExtra("url", VideoURL);
                    intent.putExtra("freespace", location.toString());
                    intent.putExtra("id", location.getParkingid());
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
                intent.putExtra("lat", latitude);
                intent.putExtra("lng", longitude);
                intent.putExtra("go", args);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);








            }
        });



    }
}
