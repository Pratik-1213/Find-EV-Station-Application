package com.example.evstations.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evstations.DBClass;
import com.example.evstations.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EVStationActivity extends AppCompatActivity {


    private EditText etxtLatitude, etxtLongitude, etxtName, etxtLocation, etxtCity;
    private Button buttonOpenMap, btnSubmit;

    String name="", location="", city="", latitude="", longitude="";
    ProgressDialog pDialog;
    JSONArray jsonData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_v_station);
        etxtName = findViewById(R.id.etxtName);
        etxtLocation = findViewById(R.id.etxtLocation);
        etxtCity = findViewById(R.id.etxtCity);

        etxtLatitude = findViewById(R.id.etxtLatitude);
        etxtLongitude = findViewById(R.id.etxtLongitude);
        buttonOpenMap = findViewById(R.id.buttonOpenMap);
        btnSubmit = findViewById(R.id.btnSubmit);

        Intent intent = getIntent();
        if (intent.hasExtra("latitude")) {
            Double lat = getIntent().getDoubleExtra("latitude", 16.7050);
            Double longi = getIntent().getDoubleExtra("longitude", 74.2433);
            etxtLatitude.setText(""+lat);
            etxtLongitude.setText(""+longi);
        }

        buttonOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // checkLocationPermission();
                Intent intent = new Intent(EVStationActivity.this, MarkerDraggingActivity.class);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Station");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void submitData()
    {
        name = etxtName.getText().toString();
        if(name.equals("")) {
            etxtName.setError("Please enter Station Name");
            etxtName.requestFocus();
            return;
        }
        location = etxtLocation.getText().toString();
        if(location.equals("")) {
            etxtLocation.setError("Please enter location Name");
            etxtLocation.requestFocus();
            return;
        }
        city = etxtCity.getText().toString();
        if(city.equals("")) {
            etxtCity.setError("Please enter city.");
            etxtCity.requestFocus();
            return;
        }
        latitude = etxtLatitude.getText().toString();
        if(latitude.equals("")) {
            etxtLatitude.setError("Latitude is empty");
            etxtLatitude.requestFocus();
            return;
        }
        longitude = etxtLongitude.getText().toString();
        if(longitude.equals("")) {
            etxtLongitude.setError("Longitude is empty");
            etxtLongitude.requestFocus();
            return;
        }
        pDialog = new ProgressDialog(EVStationActivity.this);
        pDialog.setMessage("validating your details, please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlAddStation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        Log.d("Response", ">> "+response);
                        pDialog.dismiss();
                        try {
                            jsonObject = new JSONObject(response);

                            Log.e( "onResponse: ", jsonObject.getString("status") );

                            if(jsonObject.getString("status").equals("success")) {

                                Intent intent = new Intent(getApplicationContext(), EVStationsActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "Station Added...", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Station Not Added ...Failed...", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Check Internet Connection...", Toast.LENGTH_LONG).show();
                            Log.e("Exception", ">> "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Log.e("Exception", error.toString());
                        Toast.makeText(getApplicationContext(), "Check Internet Connection...", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("location", location);
                params.put("city", city);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                Log.e("Params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


}