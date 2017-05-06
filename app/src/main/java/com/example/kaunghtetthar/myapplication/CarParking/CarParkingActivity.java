package com.example.kaunghtetthar.myapplication.CarParking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

    String URL_BASE = "https://maps.googleapis.com/maps/api/geocode/json?address=Oxford%20University,%20uk&sensor=false";
    final String URL_JSON = "/JsonReturn.php";
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

                jsonreader();

            }
        });



        jsonreader();

    }

    public void jsonreader() {

        String url = URL_BASE;

        tvjson = (TextView) findViewById(R.id.tvjson);


        Log.v("FUN2", url.toString());


        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONObject json = new JSONObject(String.valueOf(response));
                    JSONArray result = json.getJSONArray("results");


                    for(int i = 0; i < result.length(); i++) {
                        JSONObject fun1 = result.getJSONObject(i);

                        JSONObject fun2 = fun1.getJSONObject("geometry").getJSONObject("viewport")
                                .getJSONObject("southwest");

//                        JSONObject fun3 = fun2.getJSONObject("viewport");


                        String fun4 = fun2.getString("lat");



                        String fun = fun4.toString();

                        Log.v("FUN3", "id :" + fun);



                        tvjson.setText(fun);
                    }






                } catch (JSONException e) {
                    e.printStackTrace();
                }



                String list = response.toString();
//                tvjson.setText(list);



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



            final String url = URL_BASE + URL_JSON ;

            tvsite = (TextView) findViewById(R.id.tvsite);



            final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError volleyError) {

                        Log.v("FUN31", "Err: " + volleyError.getLocalizedMessage());
                    }

            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> header = new HashMap<String, String>();
                    header.put("Content-Type","application/json");

                    return header;

                }

                protected Map<String, String> getParams()
                        throws com.android.volley.AuthFailureError {
                    Map params = new HashMap<String, String>();
                    params.put("username", Username);
                    params.put("password", Password);
                    return params;
                };
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
        }


    public void site() {

        final String url = URL_BASE + paths.URL_SITES + URL_JSON;

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









