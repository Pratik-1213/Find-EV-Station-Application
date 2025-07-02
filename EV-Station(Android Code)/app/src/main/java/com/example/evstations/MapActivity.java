package com.example.evstations;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_SELECTED_LATITUDE = "extra_selected_latitude";
    public static final String EXTRA_SELECTED_LONGITUDE = "extra_selected_longitude";
    public static final int REQUEST_CODE_SELECT_LOCATION = 1001;

    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set a default location (e.g., your city)
        LatLng defaultLocation = new LatLng(37.7749, -122.4194);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));

        // Set a marker at the default location
        MarkerOptions markerOptions = new MarkerOptions().position(defaultLocation).title("Select Location");
        mMap.addMarker(markerOptions);

        // Set a click listener to handle marker click
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                // Get the selected location's coordinates
                LatLng selectedLocation = marker.getPosition();
                double selectedLatitude = selectedLocation.latitude;
                double selectedLongitude = selectedLocation.longitude;

                // Return the selected location to the calling activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_LATITUDE, selectedLatitude);
                resultIntent.putExtra(EXTRA_SELECTED_LONGITUDE, selectedLongitude);
                setResult(RESULT_OK, resultIntent);
                finish();

                return true;
            }
        });
    }
}