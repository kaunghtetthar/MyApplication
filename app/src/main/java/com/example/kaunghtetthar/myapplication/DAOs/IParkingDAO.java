package com.example.kaunghtetthar.myapplication.DAOs;

import com.example.kaunghtetthar.myapplication.model.parking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaunghtetthar on 5/2/17.
 */

public interface IParkingDAO {

    public abstract ArrayList<parking> fetchPlants(String searchTerm);

    ArrayList<parking> fetchPlants(int id) throws Exception;



    List<parking> fetchParkingsByLocation(double latitude, double longitude, double Range) throws Exception;

}
