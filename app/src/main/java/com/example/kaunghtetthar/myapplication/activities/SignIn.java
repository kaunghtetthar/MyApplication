package com.example.kaunghtetthar.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.REST.Restful;
import com.example.kaunghtetthar.myapplication.WeatherActivity;

public class SignIn extends Activity implements View.OnClickListener {

    public Button sign_in, rest, json, weather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign_in = (Button) findViewById(R.id.sign_in);
        rest = (Button) findViewById(R.id.rest);
        weather = (Button) findViewById(R.id.weather);


        sign_in.setOnClickListener(this);
        rest.setOnClickListener(this);
        weather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                startActivity(new Intent(SignIn.this,
                        MapsActivity.class));
                break;
            case R.id.rest:
                startActivity(new Intent(SignIn.this,
                        Restful.class));
                break;
            case R.id.weather:
                startActivity(new Intent(SignIn.this,
                        WeatherActivity.class));
                break;
            default:
                break;
        }
    }
}

