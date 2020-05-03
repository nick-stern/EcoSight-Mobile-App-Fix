package com.gmail.ecosight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.ecosight.Maps;
import com.gmail.ecosight.R;
import com.gmail.ecosight.ResultsAdapter;

import java.util.ArrayList;

public class Results extends AppCompatActivity {

    ListView resultsListView;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ArrayList<String> name;
    ArrayList<String> filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsListView = (ListView) findViewById(R.id.resultListView);
        name = getIntent().getStringArrayListExtra("name");
        latitude = getIntent().getStringArrayListExtra("lat");
        longitude = getIntent().getStringArrayListExtra("lon");
        filePath = getIntent().getStringArrayListExtra("fp");

        /*
        Log.d("Check", "Size of name array list: "+name.size());
        Log.d("Check", "Size of Lat array list: "+latitude.size());
        Log.d("Check", "Size of Lon array list: "+longitude.size());
        */
        // uses the new adapter to display the results
        ResultsAdapter resultsAdapter = new ResultsAdapter(this, latitude, longitude, name, filePath);
        resultsListView.setAdapter(resultsAdapter);

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /*
             * @param parent The AdapterView where the click happened.
             * @param view The view within the AdapterView that was clicked (this
             *            will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id The row id of the item that was clicked.
             */
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int x = view.getId();
                System.out.println(x);

                Intent mapIntent = new Intent(getApplicationContext(), Maps.class);
                mapIntent.putExtra("lat",latitude);
                mapIntent.putExtra("lon",longitude);
                mapIntent.putExtra("name",name);
                mapIntent.putExtra("fp",filePath);
                mapIntent.putExtra("activity", "Results");
                mapIntent.putExtra("position",position);
                startActivity(mapIntent);


            }
        });
    }

}
