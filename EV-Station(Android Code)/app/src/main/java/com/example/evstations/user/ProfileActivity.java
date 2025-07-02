package com.example.evstations.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.evstations.LoginActivity;
import com.example.evstations.R;
import com.example.evstations.SplashScreenActivity;
import com.example.evstations.admin.AdminHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    EditText etxtName, etxtEmail, etxtMobileno, etxtPassword;
    String name="", email="", mobileno="", password="";
    Button btnSubmit, btnLogout;
    String id ,  query;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        etxtName = findViewById(R.id.etxtName);
        etxtMobileno = findViewById(R.id.etxtMobileno);
        etxtEmail = findViewById(R.id.etxtEmail);
        etxtPassword = findViewById(R.id.etxtPassword);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnLogout = findViewById(R.id.btnLogout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");
        query = "SELECT CValue FROM Configuration WHERE CName = 'id'";
        id = DBClass.getSingleValue(query);

        userDetails();

    }
    public void userDetails()
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading data, please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        Log.d("Response", ">> "+response);
                        pDialog.dismiss();
                        try {
                            jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("success")) {
                                etxtName.setText(jsonObject.getString("name"));
                                etxtEmail.setText(jsonObject.getString("email"));
                                etxtMobileno.setText(jsonObject.getString("mobileno"));
                                etxtPassword.setText(jsonObject.getString("password"));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "No Information Found...", Toast.LENGTH_LONG).show();
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
                params.put("id", id);
                Log.e("Params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void logOutClick(View view) {
        String query = "DELETE FROM Configuration";
        DBClass.execNonQuery(query);
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }

    public void updateProfileClick(View view) {

        name = etxtName.getText().toString();
        if(name.equals("")) {
            etxtName.setError("Please enter Name");
            etxtName.requestFocus();
            return;
        }
        email = etxtEmail.getText().toString();
        if(email.equals("")) {
            etxtEmail.setError("Please enter Email");
            etxtEmail.requestFocus();
            return;
        }
        mobileno = etxtMobileno.getText().toString();
        if(mobileno.equals("")) {
            etxtMobileno.setError("Please enter mobile Number.");
            etxtMobileno.requestFocus();
            return;
        }
        password = etxtPassword.getText().toString();
        if(password.equals("")) {
            etxtPassword.setError("Please enter Password");
            etxtPassword.requestFocus();
            return;
        }


        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("validating your details, please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlUpdateProfile,
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

                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "Profile Updated...", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_LONG).show();
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
                params.put("id", id);
                params.put("name", name);
                params.put("email", email);
                params.put("mobileno", mobileno);
                params.put("password", password);
                Log.e("Params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
}