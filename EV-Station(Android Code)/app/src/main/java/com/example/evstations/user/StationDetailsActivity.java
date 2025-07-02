package com.example.evstations.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evstations.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mappls.sdk.maps.MapView;
import com.mappls.sdk.maps.MapplsMap;
import com.mappls.sdk.maps.OnMapReadyCallback;
import com.mappls.sdk.maps.geometry.LatLng;

public class StationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, MapplsMap.OnMapLongClickListener {

    TextView txtName, txtDetails;
    String id="", name="", city="", location="" ;
    Double s_lat=0.0, s_lon=0.0, e_lat=0.0, e_lon=0.0;

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);
        txtName = findViewById(R.id.txtName);
        txtDetails = findViewById(R.id.txtDetails);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        location = getIntent().getStringExtra("location");
        city = getIntent().getStringExtra("city");
        String lat = getIntent().getStringExtra("latitude");
        String lon = getIntent().getStringExtra("longitude");
        e_lat = Double.parseDouble(lat);
        e_lon = Double.parseDouble(lon);

        Log.d("e_lat", ""+e_lat);
        Log.d("e_lat", ""+e_lon);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            getCurrentLocation();

        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1001);
            getCurrentLocation();

        }

        txtName.setText(name);
        txtDetails.setText(location + " \n " + city);

        webView = findViewById(R.id.webView);
        initWebViewSettings();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Station Details");
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

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });
    }

    private void loadDirections(double startLat, double startLng, double endLat, double endLng) {
        String url = "https://www.google.com/maps/dir/?api=1" +
                "&origin=" + startLat + "," + startLng +
                "&destination=" + endLat + "," + endLng +
                "&travelmode=driving";  // You can change the travel mode as needed (e.g., walking, biking)

        webView.loadUrl(url);
    }


    public void btnBooknowClick(View view) {
//      String details = s_lat + " / "+ s_lon +"\n"+ e_lat+" / "+e_lon;
//      Toast.makeText(this, details,Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, BookSlotActivity.class);
        intent.putExtra("stationid", id);
        startActivity(intent);
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                s_lat = location.getLatitude();
                                s_lon = location.getLongitude();

                                loadDirections(s_lat, s_lon, e_lat, e_lon);

                            } else {
                                Toast.makeText(StationDetailsActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @Override
    public boolean onMapLongClick(@NonNull LatLng latLng) {
        return false;
    }

    @Override
    public void onMapReady(@NonNull MapplsMap mapplsMap) {

    }

    @Override
    public void onMapError(int i, String s) {

    }
}