package com.example.evstations.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.evstations.adapters.Booking;
import com.example.evstations.adapters.BookingAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewAllBookingActivity extends AppCompatActivity {

    private ArrayList<Booking> bookingArrayList;
    private BookingAdapter bookingAdapter;
    RecyclerView rview;
    ProgressDialog pDialog;
    Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_booking);
        context = getApplicationContext();
        rview =  findViewById(R.id.rview);
        list();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("All Bookings");
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

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, DBClass.urlBookings,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.d("Response ", ">> " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            bookingArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonData.length(); i++) {
                                Booking booking = new Booking();
                                JSONObject jo = jsonData.getJSONObject(i);
                                booking.id = jo.getString("id");
                                booking.station = jo.getString("name");
                                booking.slot = jo.getString("slotname");
                                booking.location = jo.getString("location");
                                booking.city = jo.getString("city");
                                booking.latitude = jo.getDouble("latitude");
                                booking.longitude = jo.getDouble("longitude");
                                booking.vehicle = jo.getString("vehicle");
                                booking.b_date = jo.getString("b_date");
                                booking.b_status = jo.getString("b_status");
                                booking.intime = jo.getString("intime");
                                booking.outtime = jo.getString("outtime");
                                booking.bookingon = jo.getString("bookingon");
                                booking.amount = jo.getString("amount");
                                booking.from = "admin";


                                bookingArrayList.add(booking);
                            }
                            bookingAdapter = new BookingAdapter(getApplicationContext(), bookingArrayList);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                            rview.setLayoutManager(layoutManager);
                            rview.setAdapter(bookingAdapter);
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
                //params.put("userid", userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}