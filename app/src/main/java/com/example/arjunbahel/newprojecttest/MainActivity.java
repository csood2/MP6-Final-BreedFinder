package com.example.arjunbahel.newprojecttest;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
public class MainActivity extends Activity {
    public final static String TAG = "BREEDPROO";


    private boolean checkPermission() {

//Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//

        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

//If the app does have this permission, then return true//

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

//If the app doesn’t have this permission, then return false//

            return false;
        }
    }



    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }

    public static RequestQueue requestQueue;


    void startAPICall(String givenImage) {
        Log.d(TAG, "here");
        try {
            Log.d(TAG, givenImage);
            Log.d(TAG, "try");
            JSONObject req = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject src = new JSONObject();
            src.put("imageUri", givenImage);
            JSONObject img = new JSONObject();
            img.put("source", src);
            req.put("requests", array);
            JSONObject myobj = new JSONObject();
            myobj.put("image", img);
            array.put(0, myobj);
            JSONObject tp = new JSONObject();
            tp.put("type", "LABEL_DETECTION");
            tp.put("maxResults", 10);
            JSONArray ftr = new JSONArray();
            ftr.put(0, tp);
            myobj.put("features", ftr);
            Log.d(TAG, "JSON created");
            Log.d(TAG, req.toString());

            final JSONObject toSend = req;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyD8I4WVl_Ob3pWBLsz_HJ8aEhQi1ordCKw",
                    toSend,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "in response thing");
                            processResponse(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, "problem");
                    Log.w(TAG, error.toString());
                }
            });
            Log.d(TAG, "before requestQueue");
            Log.d(TAG, jsonObjectRequest.toString());
            requestQueue.add(jsonObjectRequest);
            Log.d(TAG, "after requestQueue");
        } catch (Exception e) {
            Log.d(TAG, "second catch");
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!checkPermission()) {
            Toast.makeText(this, "Go to the settings and enable storage access",
                    Toast.LENGTH_LONG).show();
        }
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                String selectedImage = "https://images.pexels.com/photos/356378/pexels-photo-356378.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500";
                Log.d(TAG, selectedImage);
                startAPICall(selectedImage);
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                // Move to first row
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imgDecodableString = cursor.getString(columnIndex);
//                cursor.close();
//                ImageView imgView = (ImageView) findViewById(R.id.imgView);
//                // Set the Image in ImageView after decoding the String
//                imgView.setImageBitmap(BitmapFactory
//                                .decodeFile(imgDecodableString));


            } else {
                Toast.makeText(this, "You have not picked an Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }


    public void processResponse(JSONObject response) {
        String jsonstring = response.toString();
        try {
            Log.d(TAG, response.toString(4));
        } catch (JSONException p) {
            Log.d(TAG, "JSONException");
        }

        for(int i = 0; i < response.names().length(); i++){
            try {
                Log.v(TAG, "key = " + response.names().getString(i) + " value = " + response.get(response.names().getString(i)));
            } catch(JSONException e) {
                Log.v(TAG, "key = no key found");
        }
        }


                TextView textView = findViewById(R.id.textView);
                Log.d(TAG, "https://vision.googleapis.com/v1/images:annotate");
                textView.setText(jsonstring);
                textView.setMovementMethod(new ScrollingMovementMethod());
            }

        }