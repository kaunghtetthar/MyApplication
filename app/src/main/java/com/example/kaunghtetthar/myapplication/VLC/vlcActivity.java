package com.example.kaunghtetthar.myapplication.VLC;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kaunghtetthar.myapplication.R;

public class vlcActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlc);

        Bundle extras = getIntent().getExtras();


        if(extras!=null) {
            String url = extras.getString("url");
            Intent intent = new Intent(getApplicationContext(), vlcStreaming.class);
            intent.putExtra("videoUrl", url);
            startActivity(intent);

        } else {

        }

        Button b = (Button) findViewById(R.id.play_video);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e = (EditText) findViewById(R.id.video_url);
                String url1 = e.getText().toString();

                Intent intent = new Intent(getApplicationContext(), vlcStreaming.class);
                intent.putExtra("videoUrl", url1);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}