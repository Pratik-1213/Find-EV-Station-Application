package com.example.evstations.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evstations.DBClass;
import com.example.evstations.R;
import com.example.evstations.adapters.Station;
import com.example.evstations.adapters.StationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookSlotActivity extends AppCompatActivity {

    EditText etxtDate, etxtIntime;
    String stationid="", userid="", vehicle="", slotid="", slotname="",b_date="", intime="", outtime="", duration="" , voltage="", amount="", vehicleid="";
    Spinner spnHrs, spnSlots, spnVehicles;
    private Calendar calendar;
    ProgressDialog pDialog;
    private Map<String, String> slotMap;
    private Map<String, String> vehicleMap;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slot);
        etxtDate = findViewById(R.id.etxtDate);
        etxtIntime = findViewById(R.id.etxtIntime);
        spnHrs = findViewById(R.id.spnHrs);
        spnSlots = findViewById(R.id.spnSlots);
        spnVehicles = findViewById(R.id.spnVehicles);
        stationid = getIntent().getStringExtra("stationid");
        slotMap = new HashMap<>();
        vehicleMap = new HashMap<>();
        Button btnSubmit = findViewById(R.id.btnSubmit);
        String query = "SELECT CValue FROM Configuration WHERE CName = 'id'";
        userid = DBClass.getSingleValue(query);

        calendar = Calendar.getInstance();

        etxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        etxtIntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        String[] values = {"1", "1.30", "2", "2.30","3","3.30","4","4.30","5","5.30","6","6.30","7","7.30","8"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spn_item, values);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spnHrs.setAdapter(adapter);
        int textColor = getResources().getColor(R.color.black); // Replace with your desired color resource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spnSlots.setPopupBackgroundResource(R.drawable.shape); // Set background resource if needed
        spnHrs.getBackground().setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);

        // Set a listener for item selections
        spnHrs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedItem = (String) parentView.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        loadSlots();
        loadVehicles();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBookSlotClick();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Book Slot");
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

    public void btnBookSlotClick() {

        b_date = etxtDate.getText().toString();
        if (b_date.equals(""))
        {
            etxtDate.setError("Select Date");
            etxtDate.requestFocus();
            return;
        }
        intime = etxtIntime.getText().toString();
        if (intime.equals(""))
        {
            etxtIntime.setError("Select Time");
            etxtIntime.requestFocus();
            return;
        }
        duration = spnHrs.getSelectedItem().toString();
        if (duration.equals(""))
        {
            spnHrs.requestFocus();
            return;
        }
        slotname = spnSlots.getSelectedItem().toString();
        if (slotname.equals(""))
        {
            spnSlots.requestFocus();
            return;
        }
        vehicle = spnVehicles.getSelectedItem().toString();
        if (vehicle.equals(""))
        {
            spnVehicles.requestFocus();
            return;
        }


        String selectedTime = etxtIntime.getText().toString();
        int selectedHours = Integer.parseInt(spnHrs.getSelectedItem().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date date = sdf.parse(selectedTime);

            // Add selected hours to the time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, selectedHours);

            // Format the result time
            outtime = sdf.format(calendar.getTime());

            // Display the result
          //  Toast.makeText(this, "Result Time: " + outtime, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
           // Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show();
        }

        String selectedSlot = spnSlots.getSelectedItem().toString();
        slotid = slotMap.get(selectedSlot);
        String selectedVehicle = spnVehicles.getSelectedItem().toString();
        vehicleid = vehicleMap.get(selectedVehicle);

        pDialog = new ProgressDialog(BookSlotActivity.this);
        pDialog.setMessage("validating your details, please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlBookSlot,
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

                                Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Slot Not Booked ...Failed...", Toast.LENGTH_LONG).show();
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
                params.put("stationid", stationid);
                params.put("userid", userid);
                params.put("b_date", b_date);
                params.put("intime", intime);
                params.put("outtime", outtime);
                params.put("duration", duration);
                params.put("slotid", slotid);
                params.put("voltage", "100");
                params.put("amount",    "500");
                params.put("slotname", slotname);
                params.put("vehicleid", vehicleid);
                params.put("vehicle", vehicle);
                Log.e("Params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);



    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                R.style.CustomDatePicker,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateEditText();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        //calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void updateEditText() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etxtDate.setText(sdf.format(calendar.getTime()));
    }

    private void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        etxtIntime.setText(selectedTime);
                        logSelectedTime(selectedTime);
                    }
                },
                hour,
                minute,
                true // 24-hour format
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void logSelectedTime(String selectedTime) {
        // Log the selected time or perform further actions
       // Toast.makeText(this, "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();
    }
    public void loadSlots()
    {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlStationSlots,
                response -> {
                    JSONObject jsonObject = null;
                    Log.d("Response", ">> "+response);

                    try {
                        jsonObject = new JSONObject(response);
                        JSONArray jsonData = jsonObject.getJSONArray("data");
                        List<String> items = new ArrayList<>();
                        for (int i = 0; i < jsonData.length(); i++) {

                            JSONObject jo = jsonData.getJSONObject(i);
                            if (jo.getString("status").equals("Enable")) {
                                String item = "Voltage : " + jo.getString("voltage") + " Price : " + jo.getString("price");
                                String slotid = jo.getString("id");
                                String voltage = jo.getString("voltage");
                                String price = jo.getString("price");
                                slotMap.put(item, slotid);
                                items.add(item);
                            }


                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(BookSlotActivity.this, R.layout.spn_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnSlots.setAdapter(adapter);

                        int textColor = getResources().getColor(R.color.black); // Replace with your desired color resource
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnSlots.setPopupBackgroundResource(R.drawable.shape); // Set background resource if needed
                        spnSlots.getBackground().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);

                        // Set listener for Spinner item selection
                        spnSlots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Handle item selection if needed
//                                String selectedSlot = spnSlots.getSelectedItem().toString();
//                                slotid = slotMap.get(selectedSlot);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Handle nothing selected if needed
                            }
                        });

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Check Internet Connection...", Toast.LENGTH_LONG).show();
                        Log.e("Exception", ">> "+e);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Exception", error.toString());
                        Toast.makeText(getApplicationContext(), "Check Internet Connection...", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("stationid",stationid );
                Log.e("Params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void loadVehicles()
    {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlVehicles,
                response -> {
                    JSONObject jsonObject = null;
                    Log.d("Response", ">> "+response);

                    try {
                        jsonObject = new JSONObject(response);
                        JSONArray jsonData = jsonObject.getJSONArray("data");
                        List<String> items = new ArrayList<>();
                        for (int i = 0; i < jsonData.length(); i++) {

                            JSONObject jo = jsonData.getJSONObject(i);
                            String item = jo.getString("veh_name") +" - "+ jo.getString("veh_number");
                            String vehicleid = jo.getString("id");

                            vehicleMap.put(item,vehicleid);

                            items.add(item);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(BookSlotActivity.this, R.layout.spn_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnVehicles.setAdapter(adapter);

                        int textColor = getResources().getColor(R.color.black); // Replace with your desired color resource
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //spnSlots.setPopupBackgroundResource(R.drawable.shape); // Set background resource if needed
                        spnVehicles.getBackground().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);

                        // Set listener for Spinner item selection
                        spnVehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Handle item selection if needed
//                                String selectedVehicle = spnVehicles.getSelectedItem().toString();
//                                vehicleid = vehicleMap.get(selectedVehicle);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Handle nothing selected if needed
                            }
                        });

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Check Internet Connection...", Toast.LENGTH_LONG).show();
                        Log.e("Exception", ">> "+e);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Exception", error.toString());
                        Toast.makeText(getApplicationContext(), "Check Internet Connection...", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("userid",userid );
                Log.e("Params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}