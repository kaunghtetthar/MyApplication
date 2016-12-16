package com.example.kaunghtetthar.myapplication.CarParking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kaunghtetthar.myapplication.R;

import org.json.JSONObject;

public class CarParkingActivity extends AppCompatActivity {

    final String URL_BASE = "https://web3.cs.ait.ac.th";
    final String URL_JSON = "/1.json";
    private TextView tvjson;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_parking);

        jsonreader();

    }

        public void jsonreader() {

            final String url = URL_BASE + paths.URL_PARKINGTOTALS + URL_JSON;

            tvjson = (TextView) findViewById(R.id.tvjson);


            Log.v("FUN2", url.toString());

            final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    String list = response.toString();

                    tvjson.setText(list);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("FUN", "Err: " + error.getLocalizedMessage());
                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);


        }

    }





