package com.gmail.ecosight;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gmail.ecosight.Drone;
import com.gmail.ecosight.History;
import com.gmail.ecosight.R;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> latitude; // = {"33.5779", "29.7604", "40.7128"};
    ArrayList<String> longitude; //= {"-101.8552", "-95.3698", "-74.0060"};
    ArrayList<String> name; // = {"Lubbock, Texas", "Houston, Texas", "New York, New York"};
    ArrayList<String> filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        latitude = new ArrayList<String>();
        longitude = new ArrayList<String>();
        name = new ArrayList<String>();
        filePath = new ArrayList<String>();




        if (getIntent().getStringArrayListExtra("lat") != null){
            latitude = getIntent().getStringArrayListExtra("lat"); }
        if (getIntent().getStringArrayListExtra("lon") != null){
            longitude = getIntent().getStringArrayListExtra("lon"); }
        if (getIntent().getStringArrayListExtra("name") != null){
            name = getIntent().getStringArrayListExtra("name"); }
        if (getIntent().getStringArrayListExtra("fp") != null){
            filePath = getIntent().getStringArrayListExtra("fp"); }


        if (Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }


        //Created a Button class for the History button and set an on click listener to have the button interacted with when clicked
        Button HistBtn = (Button) findViewById(R.id.HistBtn);
        HistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New intent for the history class and starting the history activity
                Intent historyIntent = new Intent(getApplicationContext(), History.class);
                historyIntent.putExtra("lat",latitude);
                historyIntent.putExtra("lon",longitude);
                historyIntent.putExtra("name",name);
                historyIntent.putExtra("fp",filePath);
                startActivity(historyIntent);
            }
        });

        //Created a Button class for the Drone button and set an on click listener to have the button interacted with when clicked
        Button DroneBtn = (Button) findViewById(R.id.DroneBtn);
        DroneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New intent for the drone class and starting the history activity
                Intent droneIntent = new Intent(getApplicationContext(), Drone.class);
                startActivity(droneIntent);
            }
        });

        //Created a Button class for the Camera button and set an on click listener to have the button interacted with when clicked
        Button CameraBtn = (Button) findViewById(R.id.CameraBtn);
        CameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New intent for the camera class and starting the history activity
                Intent cameraIntent = new Intent(getApplicationContext(), Camera.class);
                cameraIntent.putExtra("lat",latitude);
                cameraIntent.putExtra("lon",longitude);
                cameraIntent.putExtra("name",name);
                cameraIntent.putExtra("fp",filePath);
                startActivity(cameraIntent);
            }
        });
    }
}