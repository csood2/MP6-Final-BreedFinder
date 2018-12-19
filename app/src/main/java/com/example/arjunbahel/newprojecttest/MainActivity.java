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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

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
    public final static String TAG = "BREEDPRO";


    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

            return false;
        }
    }



    private static int RESULT_LOAD_IMG = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void loadImagefromGallery(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        onActivityResult();

    }

    public static RequestQueue requestQueue;

    public void errorToast() {
        Toast.makeText(this, "Sorry, URL not valid", Toast.LENGTH_SHORT).show();
    }




    void startAPICall(String givenImage) {
        Toast.makeText(this, "Detecting Breed... ", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "here");
        try {
            ImageView imageView = findViewById(R.id.imageView2);
            Picasso.get().load(givenImage).into(imageView);
                    //.load(givenImage).into(imageView);
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
            tp.put("maxResults", 5);
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
                    errorToast();
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



    protected void onActivityResult() {

        try {






            EditText edit = findViewById(R.id.editText);
            String selectedImage = edit.getText().toString();
            Log.d(TAG, selectedImage);
            startAPICall(selectedImage);
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(edit.getWindowToken(), 0);

            String[] filePathColumn = {MediaStore.Images.Media.DATA};




        } catch (Exception e) {
Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }




    public void processResponse(JSONObject response) {
        JSONArray arr = new JSONArray();
        try {
            arr = response.getJSONArray("responses");
        } catch (Exception e) {
            Log.d(TAG, "No response named array found");
        }
        JSONObject annotations = new JSONObject();
        try {
            annotations =  arr.getJSONObject(0);


        } catch (Exception a) {
            Log.d(TAG, "No 0 element  in the responses array");
        }

        JSONArray arrayofannotations = new JSONArray();

        try {
            arrayofannotations = annotations.getJSONArray("labelAnnotations");
        } catch (Exception t) {
            Log.d(TAG, "did not extract array");
        }
        JSONObject list4 = new JSONObject();
        List<String> desc = new ArrayList<>();
        for (int kl = 0; kl < 5; kl++) {
            try {
                list4 = arrayofannotations.getJSONObject(kl);
            } catch (Exception l) {
                Log.d(TAG, "item in annottation array not found");
            }
            try {
                String descthis;
                descthis = list4.get("description").toString();
                desc.add(descthis);
                desc.add("\n");
                Log.d(TAG, descthis);
            } catch (Exception j)  {
                Log.d(TAG, "Did not get desc");
            }
        }

        try {
            Log.d(TAG, arrayofannotations.toString(4));
        } catch (JSONException p) {
            Log.d(TAG, "JSONException");
        }



                TextView textView = findViewById(R.id.textView);
                Log.d(TAG, "https://vision.googleapis.com/v1/images:annotate");
                try {
                    String setText = desc.get(8).substring(0, 1).toUpperCase() + desc.get(8).substring(1, desc.get(8).length());
                    textView.setText("It's a " + setText + "!");


                } catch (Exception e) {
                    Toast.makeText(this, "URL not valid", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "unable to print");
        }
                textView.setMovementMethod(new ScrollingMovementMethod());
            }

        }
