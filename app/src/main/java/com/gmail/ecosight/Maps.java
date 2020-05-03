package com.gmail.ecosight;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.gmail.ecosight.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Vector;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ArrayList<String> name;
    ArrayList<String> filePath;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        name = getIntent().getStringArrayListExtra("name");
        latitude = getIntent().getStringArrayListExtra("lat");
        longitude = getIntent().getStringArrayListExtra("lon");
        filePath = getIntent().getStringArrayListExtra("fp");

        // If the call to this activity was from History it does the first if, if it was from Results it does the second
       if(name.size() == 0){
           float zoomLevel = 3.0f;
           LatLng x = new LatLng(39.8283, -98.5795);
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, zoomLevel));
       }
        else if(getIntent().getStringExtra("activity").equals("History")) {
            float zoomLevel = 13.0f;
            // Add a marker to location and move the camera
            for(int i = 0; i < latitude.size(); i++) {
                LatLng x = new LatLng(Double.valueOf(latitude.get(i)), Double.valueOf(longitude.get(i)));
                mMap.addMarker(new MarkerOptions().position(x).title(name.get(i)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, zoomLevel));
            }
        } else if(getIntent().getStringExtra("activity").equals("Results")){
            position = getIntent().getIntExtra("position", 0);
            float zoomLevel = 13.0f;
            // Add a marker to location and move the camera
            for(int i = 0; i < latitude.size(); i++) {
                LatLng x = new LatLng(Double.valueOf(latitude.get(i)), Double.valueOf(longitude.get(i)));
                mMap.addMarker(new MarkerOptions().position(x).title(name.get(i)));
            }
            LatLng y = new LatLng(Double.valueOf(latitude.get(position)), Double.valueOf(longitude.get(position)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(y, zoomLevel));
        }

    }

}
