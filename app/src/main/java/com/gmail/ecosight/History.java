package com.gmail.ecosight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.ecosight.Maps;
import com.gmail.ecosight.R;
import com.gmail.ecosight.Results;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ArrayList<String> name;
    ArrayList<String> filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        latitude = getIntent().getStringArrayListExtra("lat");
        longitude = getIntent().getStringArrayListExtra("lon");
        name = getIntent().getStringArrayListExtra("name");
        filePath = getIntent().getStringArrayListExtra("fp");

        Button mapBtn = (Button) findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getApplicationContext(), Maps.class);
                //latitude = getIntent().getStringArrayListExtra("lat");
                //longitude = getIntent().getStringArrayListExtra("lon");
                //name = getIntent().getStringArrayListExtra("name");
                mapIntent.putExtra("lat",latitude);
                mapIntent.putExtra("lon",longitude);
                mapIntent.putExtra("name",name);
                mapIntent.putExtra("fp",filePath);
                mapIntent.putExtra("activity", "History");
                startActivity(mapIntent);
            }
        });

        // display results class
        Button resultsBtn = (Button) findViewById(R.id.resultsBtn);
        resultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultsIntent = new Intent(getApplicationContext(), Results.class);
               // latitude = getIntent().getStringArrayListExtra("lat");
                //longitude = getIntent().getStringArrayListExtra("lon");
               // name = getIntent().getStringArrayListExtra("name");
                resultsIntent.putExtra("lat",latitude);
                resultsIntent.putExtra("lon",longitude);
                resultsIntent.putExtra("name",name);
                resultsIntent.putExtra("fp",filePath);
                startActivity(resultsIntent);
            }
        });
    }
}
