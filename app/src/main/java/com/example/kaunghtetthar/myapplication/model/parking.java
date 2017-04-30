package com.example.kaunghtetthar.myapplication.model;

/**
 * Created by kaunghtetthar on 4/25/17.
 */

public class parking {


    final String DRAWABLE = "drawable/";


    public String getImgUrl() {
        return DRAWABLE + locationImrUrl;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getFreeSpace() {
        return freespaces;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocationImrUrl() {
        return locationImrUrl;
    }

    public String getFreespacesTitle() {
        return freespacesTitle;
    }

    public String getVideoStreaming() {return
            videoStreaming;}



    private float longitude;
    private float latitude;
    private String locationTitle;
    private String locationAddress;
    private String locationImrUrl;
    private String videoStreaming;
    private String freespacesTitle;
    private int freespaces;

    public parking(float latitude, float longitude, String freespacesTitle, int freespaces, String locationTitle, String locationAddress, String locationImrUrl, String videoStreaming) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationTitle = locationTitle + longitude + latitude;
        this.locationAddress = locationAddress;
        this.locationImrUrl = locationImrUrl;
        this.videoStreaming = videoStreaming;
        this.freespaces =  freespaces ;
        this.freespacesTitle = freespacesTitle + freespaces;

    }
}
