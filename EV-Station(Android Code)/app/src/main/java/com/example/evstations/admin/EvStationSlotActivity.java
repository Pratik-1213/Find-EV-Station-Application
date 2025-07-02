package com.example.evstations.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evstations.DBClass;
import com.example.evstations.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EvStationSlotActivity extends AppCompatActivity {

    EditText etxtVoltage, etxtPrice;
    String voltage="" , price="" ,status="";
    RadioButton rdEnable, rdDisable;
    ProgressDialog pDialog;

    public String stationid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_station_slot);
        etxtVoltage = findViewById(R.id.etxtVoltage);
        etxtPrice = findViewById(R.id.etxtPrice);
        rdEnable = findViewById(R.id.rdEnable);
        rdDisable = findViewById(R.id.rdDisable);
        stationid = getIntent().getStringExtra("stationid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Station Slot");
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

    public void btnSubmitClick(View view) {
        voltage = etxtVoltage.getText().toString();
        if(voltage.equals("")) {
            etxtVoltage.setError("Please enter Voltage");
            etxtVoltage.requestFocus();
            return;
        }
        price = etxtPrice.getText().toString();
        if(price.equals("")) {
            etxtPrice.setError("Please enter price");
            etxtPrice.requestFocus();
            return;
        }
        if (rdEnable.isChecked())
        {
            status = rdEnable.getText().toString();
        }
        if (rdDisable.isChecked())
        {
            status = rdDisable.getText().toString();
        }

        pDialog = new ProgressDialog(EvStationSlotActivity.this);
        pDialog.setMessage("validating your details, please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlAddStationSlot,
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
                                Toast.makeText(getApplicationContext(), "Station Slot Added ...", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Station Slot Not Added ...Failed...", Toast.LENGTH_LONG).show();
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
                params.put("voltage", voltage);
                params.put("price", price);
                params.put("stationid", stationid);
                params.put("status", status);
                Log.e("Params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
}