package com.example.evstations;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evstations.admin.AdminHomeActivity;
import com.example.evstations.user.UserHomeActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etxtEmail, etxtPassword;

    String id="", name="", mobileno="", email="", password="", usertype="";
    ProgressDialog pDialog;
    JSONArray jsonData = null;
    Intent intent;
    boolean loginSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etxtEmail = findViewById(R.id.etxtEmail);
        etxtPassword = findViewById(R.id.etxtPassword);

    }

    public void btnLoginClick(View view) {

        email = etxtEmail.getText().toString();
        password = etxtPassword.getText().toString();
        if (email.equals("")) {
            etxtEmail.setError("Please Enter  Email.");
            etxtEmail.requestFocus();
            return;
        }
        if (password.equals("")) {
            etxtPassword.setError("Please Enter  Password.");
            etxtPassword.requestFocus();
            return;
        }

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("validating your details, please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", ">> " + response); // Log the response to check its content
                        pDialog.dismiss();

                        try {
                            // Check if the response is in JSON format
                            if (response.trim().startsWith("{") || response.trim().startsWith("[")) {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean loginSuccess = false;

                                Log.d("onResponse", "Status: " + jsonObject.getString("status"));

                                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                    // Extract data from JSON
                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String email = jsonObject.getString("email");
                                    String usertype = jsonObject.getString("usertype");
                                    String mobileno = jsonObject.getString("mobileno");
                                    loginSuccess = true;

                                    // Clear existing configuration and add new values
                                    String query = "DELETE FROM Configuration";
                                    DBClass.execNonQuery(query);

                                    // Insert new configuration values safely
                                    String[] configNames = {"usertype", "id", "name", "email", "mobileno"};
                                    String[] configValues = {usertype, id, name, email, mobileno};

                                    for (int i = 0; i < configNames.length; i++) {
                                        query = "INSERT INTO Configuration(CName, CValue) ";
                                        query += "VALUES('" + configNames[i] + "', '" + configValues[i].replace("'", "''") + "')";
                                        DBClass.execNonQuery(query);
                                    }

                                    // Redirect user based on their type
                                    Intent intent;
                                    if (usertype.equals("user")) {
                                        intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                                    } else {
                                        intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                                    }
                                    startActivity(intent);
                                    finish();

                                    Toast.makeText(getApplicationContext(), "Login Successfully...", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Email not found...", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // Handle non-JSON responses
                                Log.e("Invalid Response", "Response is not in JSON format: " + response);
                                Toast.makeText(getApplicationContext(), "Unexpected response from server. Please try again later.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // Log and notify parsing errors
                            Log.e("JSONException", "Failed to parse response: " + response, e);
                            Toast.makeText(getApplicationContext(), "Check Internet Connection...", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Log.e("VolleyError", error.toString());
                        Toast.makeText(getApplicationContext(), "Check Internet Connection...", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                Log.d("Params", params.toString());
                return params;
            }
        };

// Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void btnRegistrationClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}