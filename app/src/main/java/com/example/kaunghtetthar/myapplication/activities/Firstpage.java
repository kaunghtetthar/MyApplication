package com.example.kaunghtetthar.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kaunghtetthar.myapplication.CarParking.CarParkingActivity;
import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.REST.Restful;
import com.example.kaunghtetthar.myapplication.Weather.WeatherActivity;

public class Firstpage extends Activity implements View.OnClickListener {

    public Button rest, json, weather, carparking, map, sign_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        map = (Button) findViewById(R.id.find_map);
        rest = (Button) findViewById(R.id.rest);
        weather = (Button) findViewById(R.id.weather);
        carparking = (Button) findViewById(R.id.carparking);
        sign_in = (Button) findViewById(R.id.sign_in);



        map.setOnClickListener(this);
        rest.setOnClickListener(this);
        weather.setOnClickListener(this);
        carparking.setOnClickListener(this);
        sign_in.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_map:
                startActivity(new Intent(Firstpage.this,
                        MapsActivity.class));
                break;
            case R.id.rest:
                startActivity(new Intent(Firstpage.this,
                        Restful.class));
                break;
            case R.id.weather:
                startActivity(new Intent(Firstpage.this,
                        WeatherActivity.class));
                break;
            case R.id.carparking:
                startActivity(new Intent(Firstpage.this,
                        CarParkingActivity.class));
                break;
            case R.id.sign_in:
                startActivity(new Intent(Firstpage.this,
                        CarParkingActivity.class));
                break;
            default:
                break;
        }
    }
}

