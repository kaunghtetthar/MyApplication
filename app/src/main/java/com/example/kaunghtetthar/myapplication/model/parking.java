package com.example.kaunghtetthar.myapplication.model;

/**
 * Created by kaunghtetthar on 4/25/17.
 */

public class parking {


    public parking() {

    }


    final String DRAWABLE = "drawable/";


    public String getImgUrl() {
        return DRAWABLE + locationImrUrl;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getFreeSpace() {

        return freespaces;
    }



    public double getLatitude() {
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





    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public void setLocationImrUrl(String locationImrUrl) {
        this.locationImrUrl = locationImrUrl;
    }

    public void setVideoStreaming(String videoStreaming) {
        this.videoStreaming = videoStreaming;
    }

    public void setFreespacesTitle(String freespacesTitle) {
        this.freespacesTitle = freespacesTitle + freespaces;
    }

    public int getTotalslots() {
        return totalslots;
    }

    public void setTotalslots(int totalslots) {
        this.totalslots = totalslots;
    }

    public void setFreespaces(int freespaces) {
        this.freespaces = freespaces;
    }

    private String locationImrUrl;
    private String videoStreaming;
    private String freespacesTitle;

    public String getTotalslotsTitle() {
        return totalslotsTitle;
    }

    public void setTotalslotsTitle(String totalslotsTitle) {
        this.totalslotsTitle = totalslotsTitle;
    }

    private String totalslotsTitle;
    private String name;
    private int freespaces;
    private int totalslots;
    private double longitude;
    private double latitude;
    private String locationTitle;
    private String locationAddress;

    public int getParkingid() {
        return parkingid;
    }

    public void setParkingid(int parkingid) {
        this.parkingid = parkingid;
    }

    private int parkingid;

    public String toString() {

    return String.valueOf(parkingid);


        }

    public String nourl() {

        return  " No parking data ";


    }
}
