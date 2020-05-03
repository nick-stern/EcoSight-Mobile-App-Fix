package com.gmail.ecosight;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class Camera extends AppCompatActivity {
    Button TakePicBtn;
    Button SendPhotoBtn;
    ImageView imageview;
    String pathToFile;

    FusedLocationProviderClient fusedLocationProviderClient;
    Double currentLat;
    Double currentLon;

    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ArrayList<String> name;
    ArrayList<String> filePath;

    @Override
    public void onBackPressed() {
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainActivityIntent.putExtra("lat",latitude);
        mainActivityIntent.putExtra("lon",longitude);
        mainActivityIntent.putExtra("name",name);
        mainActivityIntent.putExtra("fp",filePath);
        //Log.d("Check", "Size of name array list: "+name.size());
       //Log.d("Check", "Size of Lat array list: "+latitude.size());
        //Log.d("Check", "Size of Lon array list: "+longitude.size());
        startActivity(mainActivityIntent);
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        name = getIntent().getStringArrayListExtra("name");
        latitude = getIntent().getStringArrayListExtra("lat");
        longitude = getIntent().getStringArrayListExtra("lon");
        filePath = getIntent().getStringArrayListExtra("fp");


        TakePicBtn = findViewById(R.id.TakePicBtn);
        /*
        if (Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        if(ActivityCompat.checkSelfPermission(Camera.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Camera.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }
*/
        TakePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                takePictureAction();
            }
        });
        imageview = findViewById(R.id.image);

        SendPhotoBtn = findViewById(R.id.SendPhotoBtn);
        SendPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultsIntent = new Intent(getApplicationContext(), Results.class);
                resultsIntent.putExtra("lat",latitude);
                resultsIntent.putExtra("lon",longitude);
                resultsIntent.putExtra("name",name);
                resultsIntent.putExtra("fp",filePath);

                //send image to python socket server
                send sendimg = new send();
                sendimg.execute();

                startActivity(resultsIntent);
            }
        });

    }

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){

                    try {
                        Geocoder geocoder = new Geocoder(Camera.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        currentLat = addresses.get(0).getLatitude();
                        currentLon = addresses.get(0).getLongitude();

                        //Converting to string and adding to the existing arrays
                        String currentLatS = Double.toString(currentLat);
                        String currentLonS = Double.toString(currentLon);

                        latitude.add(currentLatS);
                        longitude.add(currentLonS);





                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                File f = new File(pathToFile);
                imageview.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }
        }
    }

    private void takePictureAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            photoFile = createPhotoFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Camera.this, "com.gmail.ecosight.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, 1);
            }

        }
    }

    private File createPhotoFile() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timestamp + "_";
        //THIS WILL NEED TO BE CHANGED!!!!!!!!!!!!!!!
        name.add(fileName);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(fileName, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("myLog", "Excep: "+e.toString());
        }
        pathToFile = image.getAbsolutePath();
        filePath.add(pathToFile);
        return image;
    }

    //class to communicate with python server via a socket
    class send extends AsyncTask<Void,Void,Void> {
        //socket variable
        Socket s;

        @Override
        protected Void doInBackground(Void...params){
            try { //TODO : change to get IP address of current machine
                s = new Socket("10.10.188.13",8000); //connects to Leidy's IP address, will need to be changed
                InputStream input = new FileInputStream(pathToFile);

                try {
                    try {
                        //Reads bytes all together
                        int bytesRead;
                        while ((bytesRead = input.read()) != -1) {
                            s.getOutputStream().write(bytesRead); //Writes bytes to output stream
                        }
                    } finally {
                        //Flushes and closes socket
                        s.getOutputStream().flush();
                        s.close();
                    }
                } finally {
                    input.close();
                }
            } catch (UnknownHostException e) {
                System.out.println("Fail");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
            }
            return null;
        }
    }
    
}
