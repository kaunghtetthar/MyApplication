package com.example.kaunghtetthar.myapplication.MapsDirection;

import java.util.List;

/**
 * Created by kaunghtetthar on 12/13/16.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> routes);
}

