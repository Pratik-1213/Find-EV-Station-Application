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
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evstations.DBClass;
import com.example.evstations.R;
import com.example.evstations.adapters.Slot;
import com.example.evstations.adapters.SlotAdapter;
import com.example.evstations.adapters.Vehicle;
import com.example.evstations.adapters.VehicleAdapter;
import com.example.evstations.admin.EvStationSlotActivity;
import com.example.evstations.admin.EvStationSlotsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VehiclesActivity extends AppCompatActivity {

    private ArrayList<Vehicle> vehicleArrayList;
    private VehicleAdapter vehicleAdapter;
    RecyclerView rview;
    ProgressDialog pDialog;
    Context context = null;
    FloatingActionButton fab;
    public String userid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        context = getApplicationContext();
        rview =  findViewById(R.id.rview);
        fab = findViewById(R.id.floatingActionButton);
        String query = "SELECT CValue FROM Configuration WHERE CName = 'id'";
        userid = DBClass.getSingleValue(query);
        list();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehiclesActivity.this, AddVehicleActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Vehicles");
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

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, DBClass.urlVehicles,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.d("Response ", ">> " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            vehicleArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonData.length(); i++) {
                                Vehicle vehicle = new Vehicle();
                                JSONObject jo = jsonData.getJSONObject(i);
                                vehicle.id = jo.getString("id");
                                vehicle.userid = jo.getString("userid");
                                vehicle.veh_name = jo.getString("veh_name");
                                vehicle.veh_number = jo.getString("veh_number");


                                vehicleArrayList.add(vehicle);
                            }
                            vehicleAdapter = new VehicleAdapter(getApplicationContext(), vehicleArrayList);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                            rview.setLayoutManager(layoutManager);
                            rview.setAdapter(vehicleAdapter);
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
                params.put("userid", userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}