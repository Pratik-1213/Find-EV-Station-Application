package com.example.evstations.admin;

import static com.mappls.sdk.maps.Mappls.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.evstations.DBClass;
import com.example.evstations.R;
import com.mappls.sdk.maps.MapView;
import com.mappls.sdk.maps.Mappls;
import com.mappls.sdk.maps.MapplsMap;
import com.mappls.sdk.maps.OnMapReadyCallback;
import com.mappls.sdk.maps.Style;
import com.mappls.sdk.maps.camera.CameraUpdateFactory;
import com.mappls.sdk.maps.geometry.LatLng;
import com.mappls.sdk.maps.utils.BitmapUtils;
import com.mappls.sdk.plugin.annotation.OnSymbolDragListener;
import com.mappls.sdk.plugin.annotation.Symbol;
import com.mappls.sdk.plugin.annotation.SymbolManager;
import com.mappls.sdk.plugin.annotation.SymbolOptions;
import com.mappls.sdk.services.account.MapplsAccountManager;

public class MarkerDraggingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapplsMap mapplsMap;
    private LatLng latLng = new LatLng(16.7050, 74.2433);
    private SymbolManager symbolManager;
    public Double latitude = 16.7050, longitude = 74.2433;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapplsAccountManager.getInstance().setRestAPIKey("3b85112cd7196f338bcfafc7a00eeff6");
        MapplsAccountManager.getInstance().setMapSDKKey("3b85112cd7196f338bcfafc7a00eeff6");
        MapplsAccountManager.getInstance().setAtlasClientId("96dHZVzsAutI2dYJ4tJlzaa886K6I2iQ2NLW6dFa93wUbjPFlH2qBebJx2uGbqpY730umVH6PLgAUc5tTw59Hw==");
        MapplsAccountManager.getInstance().setAtlasClientSecret("lrFxI-iSEg8zNE_bzFpz_7Jv0B8_kx8n4wJEOhdu9W0_1NZKxj35xvdsj935LW-DeJyv5X_ATjCooSR1_czuwgAzC4JRYG7p");
        Mappls.getInstance(this);
        setContentView(R.layout.activity_marker_dragging);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull MapplsMap mapplsMap) {
        this.mapplsMap = mapplsMap;
        mapplsMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        initMarker();
    }

    private void initMarker() {
        mapplsMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                symbolManager = new SymbolManager(mapView, mapplsMap, style);
                SymbolOptions symbolOptions = new SymbolOptions()
                        .position(latLng)
                        .icon(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(MarkerDraggingActivity.this, R.drawable.placeholder)))
                        .draggable(true);
                symbolManager.setIconIgnorePlacement(false);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.create(symbolOptions);
                symbolManager.addDragListener(new OnSymbolDragListener() {
                    @Override
                    public void onAnnotationDragStarted(Symbol symbol) {

                    }

                    @Override
                    public void onAnnotationDrag(Symbol symbol) {

                    }

                    @Override
                    public void onAnnotationDragFinished(Symbol symbol) {

                        Log.d("latitude"," - "+ symbol.getGeometry().latitude());
                        Log.d("longitude"," - "+ symbol.getGeometry().longitude());
                        latitude = symbol.getGeometry().latitude();
                        longitude = symbol.getGeometry().longitude();
                        Toast.makeText(MarkerDraggingActivity.this, symbol.getPosition().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onMapError(int i, String s) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(symbolManager != null) {
            symbolManager.onDestroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void btnGetLatlongClick(View view) {
        Intent intent = new Intent(this, EVStationActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}