package com.gmail.ecosight;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ResultsAdapter extends BaseAdapter {

    // this class prepares the results to be displayed on the Results activity

    LayoutInflater mInflater;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ArrayList<String> name;
    ArrayList<String> filePath;

    public ResultsAdapter(Context c, ArrayList<String> la, ArrayList<String> lo, ArrayList<String> city, ArrayList<String> fp){
        latitude = la;
        longitude = lo;
        name = city;
        filePath = fp;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int i) {
        return name.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        // get details of format from results_listview_detail.xml
        View v = mInflater.inflate(R.layout.results_listview_detail, null);
        TextView citiesTextView = (TextView) v.findViewById(R.id.cityTextView);
        TextView latTextView = (TextView) v.findViewById(R.id.latTextView);
        TextView longTextView = (TextView) v.findViewById(R.id.longTextView);
        ImageView PlantImageResult = (ImageView) v.findViewById(R.id.PlantImageResult);

        String city = name.get(i);
        String lat = latitude.get(i);
        String lon = longitude.get(i);

        File f = new File(filePath.get(i));
        PlantImageResult.setImageURI(Uri.fromFile(f));

        citiesTextView.setText(city);
        latTextView.setText(lat);
        longTextView.setText(lon);


        return v;
    }

}
