package com.ropkastudios.android.thegeographyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
public class MapViewer extends AppCompatActivity {

    public static final String ARG_LATITUDE = "arg_map_data_latitude";
    public static final String ARG_LONGITUDE = "arg_map_data_longtude";
    public static final String ARG_ZOOM = "arg_map_data_title";
    public static final String ARG_TILT = "arg_map_data_tilt";
    public static final String ARG_BEARING = "arg_map_data_bearing";
    public static final String ARG_TITLE = "arg_title";

    MapView mMapView;
    GoogleMap mGMap;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_data_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pager_map);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mMapView = (MapView) findViewById(R.id.map_data_result_map_view);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Intent intent = getIntent();
                final LatLng userInputMap = new LatLng(
                        intent.getDoubleExtra(MapViewer.ARG_LATITUDE, 0),
                        intent.getDoubleExtra(MapViewer.ARG_LONGITUDE, 0));
                CameraPosition position = new CameraPosition.Builder()
                        .target(userInputMap)
                        .bearing(intent.getFloatExtra(MapViewer.ARG_BEARING, 0))
                        .zoom(intent.getFloatExtra(MapViewer.ARG_ZOOM, 0))
                        .tilt(intent.getFloatExtra(MapViewer.ARG_TILT, 0))
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                mGMap = googleMap;
            }
        });

        setTitle(getIntent().getStringExtra(ARG_TITLE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
