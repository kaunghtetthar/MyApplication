package com.example.kaunghtetthar.myapplication.model;

/**
 * Created by kaunghtetthar on 12/12/16.
 */

public class myapp {

    final String DRAWABLE = "drawable/";

    public String getImgUrl() {
        return DRAWABLE + locationImrUrl;
    }

    public float getLongitude() {
        return longitude;
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

    private float longitude;
    private float latitude;
    private String locationTitle;
    private String locationAddress;
    private String locationImrUrl;

    public myapp(float latitude, float longitude, String locationTitle, String locationAddress, String locationImrUrl) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationTitle = locationTitle + longitude + latitude;
        this.locationAddress = locationAddress;
        this.locationImrUrl = locationImrUrl;
    }


}
