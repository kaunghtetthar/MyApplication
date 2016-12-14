package com.example.kaunghtetthar.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.kaunghtetthar.myapplication.R;
import com.example.kaunghtetthar.myapplication.REST.Restful;

public class SignIn extends AppCompatActivity {


    public Button sign_in;
    public Button rest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign_in = (Button) findViewById(R.id.sign_in);
        rest = (Button) findViewById(R.id.rest);


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this,Firstpage.class);
                startActivity(intent);

            }
        });

        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this,Restful.class);
                startActivity(intent);

            }
        });

    }
}
