package com.example.evstations.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evstations.DBClass;
import com.example.evstations.R;
import com.example.evstations.adapters.EvstationsAdapter;
import com.example.evstations.adapters.Station;
import com.example.evstations.adapters.StationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindStationsActivity extends AppCompatActivity {

    private ArrayList<Station> stationArrayList;
    private EvstationsAdapter evstationsAdapter;
    RecyclerView rview;
    public ProgressDialog pDialog;
    Context context = null;
    String city="";
    Double lat, lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_stations);
        Intent intent = getIntent();
        if (intent.hasExtra("city")) {
            city = getIntent().getStringExtra("city");
        }
        else if(intent.hasExtra("lat"))
        {
            lat = getIntent().getDoubleExtra("lat", 16.7050);
            lon = getIntent().getDoubleExtra("lon", 74.2433);
        }
        context = getApplicationContext();
        rview =  findViewById(R.id.rview);
        list();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Find Stations");
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

    public void list()
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("downloading, please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        pDialog.show();

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, DBClass.urlFindStations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.d("Response ", ">> " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            stationArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonData.length(); i++) {
                                Station station = new Station();
                                JSONObject jo = jsonData.getJSONObject(i);
                                station.id = jo.getString("id");
                                station.name = jo.getString("name");
                                station.location = jo.getString("location");
                                station.city = jo.getString("city");
                                station.latitude = jo.getString("latitude");
                                station.longitude = jo.getString("longitude");

                                stationArrayList.add(station);
                            }
                            evstationsAdapter = new EvstationsAdapter(getApplicationContext(), stationArrayList);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                            rview.setLayoutManager(layoutManager);
                            rview.setAdapter(evstationsAdapter);
                        } catch (Exception e) {
                            Log.e("Exception ", ">> " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error ", ">> " + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (city.isEmpty())
                {
                    params.put("lat", String.valueOf(lat));
                    params.put("lon", String.valueOf(lon));
                }
                else
                {
                    params.put("city", city);
                }

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}