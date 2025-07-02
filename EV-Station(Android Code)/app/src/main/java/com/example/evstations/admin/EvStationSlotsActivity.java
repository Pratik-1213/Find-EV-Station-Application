package com.example.evstations.admin;

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
import com.example.evstations.adapters.Station;
import com.example.evstations.adapters.StationAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EvStationSlotsActivity extends AppCompatActivity {

    private ArrayList<Slot> slotArrayList;
    private SlotAdapter slotAdapter;
    RecyclerView rview;
    ProgressDialog pDialog;
    Context context = null;
    FloatingActionButton fab;
    public String stationid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_station_slots);
        context = getApplicationContext();
        rview =  findViewById(R.id.rview);
        fab = findViewById(R.id.floatingActionButton);
        stationid  = getIntent().getStringExtra("id");
        list();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EvStationSlotsActivity.this, EvStationSlotActivity.class);
                intent.putExtra("stationid", stationid);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Station Slots");
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

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, DBClass.urlStationSlots,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.d("Response ", ">> " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            slotArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonData.length(); i++) {
                                Slot slot = new Slot();
                                JSONObject jo = jsonData.getJSONObject(i);
                                slot.id = jo.getString("id");
                                slot.stationid = jo.getString("stationid");
                                slot.voltage = jo.getString("voltage");
                                slot.price = jo.getString("price");
                                slot.status = jo.getString("status");

                                slotArrayList.add(slot);
                            }
                            slotAdapter = new SlotAdapter(getApplicationContext(), slotArrayList);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                            rview.setLayoutManager(layoutManager);
                            rview.setAdapter(slotAdapter);
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
                params.put("stationid", stationid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}