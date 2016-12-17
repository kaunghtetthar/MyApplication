package com.example.kaunghtetthar.myapplication.CarParking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kaunghtetthar.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CarParkingActivity extends AppCompatActivity {

    final String URL_BASE = "https://web3.cs.ait.ac.th";
    final String URL_JSON = "/1.json";
    private TextView tvjson;
    private Button http_sign_in;
    private TextView tvsite;
    private StringRequest request;
    private RequestQueue requestQueue;
    private EditText username, password;
    private static String Username = "dev";
    private static String Password = "devdev";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_parking);



        http_sign_in = (Button) findViewById(R.id.http_sign_in);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        requestQueue  = Volley.newRequestQueue(this);

        http_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonsignin();

            }
        });



        jsonreader();

    }

    public void jsonreader() {

        final String url = URL_BASE + paths.URL_PARKINGTOTALS + URL_JSON;

        tvjson = (TextView) findViewById(R.id.tvjson);


        Log.v("FUN2", url.toString());


        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String final1 = response.toString();

                try {
                    JSONObject params = new JSONObject(final1);
                    int parentArray = params.getInt("id");

                    Log.v("FUN3" , "id :" + parentArray);


                } catch (JSONException e) {
                    e.printStackTrace();
                }



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

        public void jsonsignin() {

            final String url = URL_BASE + paths.URL_SITES + URL_JSON;

            tvsite = (TextView) findViewById(R.id.tvsite);

            JSONObject params = new JSONObject();



            final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    final JSONObject root = new JSONObject();
                    JSONObject root1 = new JSONObject();

                    try {

                        JSONObject parrentArray = root1.getJSONObject("id");
                        String list = parrentArray.toString();

                        tvsite.setText(list);
                        Log.v("FUN3", "Err: " + list);

                        if (list != null) {
                            try {
                                JSONArray parentArray = root.getJSONArray("error");
                                String error = parentArray.toString();
                                Log.v("FUN3", "Error: " + error);

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("FUN3", "Err: " + error.getLocalizedMessage());
                }
            })
            {

                public Map<String, String> getParams () {
                HashMap<String, String> params = new HashMap<String, String>();
                    params.put("username" , Username);
                    params.put("password" , Password);
                return params;

            }
            };



            Volley.newRequestQueue(this).add(jsonRequest);


//            request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//
//                        String list = response.toString();
//
//                        tvsite.setText(list);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.v("FUN3", "Err: " + error.getLocalizedMessage());
//
//                }
//            })
//
//            {
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    params.put("user_username", username.getText().toString());
//                    params.put("user_password", password.getText().toString());
//                    params.put("Content-Type","application/json; charset=utf-8");
//
//                    return params;
//
//                }
//
////                @Override
////                public Map<String, String> getHeaders() throws AuthFailureError {
////                    HashMap<String, String> params = new HashMap<String, String>();
////                    return params;
////
////
////
////                }
//            };
//
//            requestQueue.add(request);
//
//
//
//
//                  Volley.newRequestQueue(this).add(jsonRequest);



//
//
//
        }





    }

//            {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    params.put("username", username.getText().toString());
//                    params.put("password", password.getText().toString());
//                    params.put("Content type", "application/x-www-form-urlencoded");
//                    return params;
//                }
//            };
//            {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    params.put("Content-Type", "application/x-www-form-urlencoded");
//                    return params;
//                }
//            };









