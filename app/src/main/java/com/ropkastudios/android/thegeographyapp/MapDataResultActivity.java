package com.ropkastudios.android.thegeographyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MapDataResultActivity extends AppCompatActivity {
    private static final String DIALOG_NONE = "dialog_data";

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 60;

    public static final String EXTRA_LATITUDE = "extra_data_latitude";
    public static final String EXTRA_LONGITUDE = "extra_data_longitude";
    public static final String EXTRA_ZOOM = "extra_data_zoom";
    public static final String EXTRA_BEARING = "extra_data_bearing";
    public static final String EXTRA_TILT = "extra_data_tilt";
    public static final String EXTRA_CURRENT_TITLE = "extra_current_title";
    public static final String EXTRA_INDEX_OF = "extra_map_index_of";

    public static final String EXTRA_FILE_PATH = "extra_map_file_path";

    public static final String ARG_TITLE = "arg_map_data_title";
    public static final String ARG_LATITUDE = "arg_mapres_data_latitude";
    public static final String ARG_LONGITUDE = "arg_mapres_data_longtude";
    public static final String ARG_ZOOM = "arg_map_datares_title";
    public static final String ARG_TILT = "arg_map_datares_tilt";
    public static final String ARG_BEARING = "arg_map_datares_bearing";
    public static final String ARG_CURRENT_TITLE = "arg_map_current_title";
    public static final String ARG_INDEX_OF = "arg_map_index_of";

    MapView mMapView;
    GoogleMap mGMap;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

        mMapView = (MapView) findViewById(R.id.map_data_result_map_view);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        }
        final Intent intent = getIntent();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                final LatLng userInputMap = new LatLng(
                        intent.getDoubleExtra(MapDataResultActivity.ARG_LATITUDE, 0),
                        intent.getDoubleExtra(MapDataResultActivity.ARG_LONGITUDE, 0));
                CameraPosition position = new CameraPosition.Builder()
                        .target(userInputMap)
                        .bearing(intent.getFloatExtra(MapDataResultActivity.ARG_BEARING, 0))
                        .zoom(intent.getFloatExtra(MapDataResultActivity.ARG_ZOOM, 0))
                        .tilt(intent.getFloatExtra(MapDataResultActivity.ARG_TILT, 0))
                        .build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                mGMap = googleMap;
            }
        });

        setTitle(getString(R.string.data_result_map));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map_result, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_map_result_done:
                CameraPosition position = mGMap.getCameraPosition();
                double latitude = position.target.latitude;
                double longitude = position.target.longitude;
                float zoom = position.zoom;
                float tilt = position.tilt;
                float bearing = position.bearing;
                Intent data = new Intent();
                data.putExtra(EXTRA_LATITUDE, latitude);
                data.putExtra(EXTRA_LONGITUDE, longitude);
                data.putExtra(EXTRA_ZOOM, zoom);
                data.putExtra(EXTRA_TILT, tilt);
                data.putExtra(EXTRA_BEARING, bearing);
                data.putExtra(EXTRA_CURRENT_TITLE, getIntent().getStringExtra(ARG_CURRENT_TITLE));
                data.putExtra(EXTRA_INDEX_OF, getIntent().getStringExtra(ARG_INDEX_OF));
                setResult(Activity.RESULT_OK, data);
                finish();
                return true;
            case R.id.menu_map_result_cancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                return true;
            case R.id.menu_map_result_scrnshot:
                takeMapSnapshot();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void snapShotSend(String filePath) {
        if (!filePath.equals("")) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_FILE_PATH, filePath);
            intent.putExtra(EXTRA_CURRENT_TITLE, getIntent().getStringExtra(ARG_CURRENT_TITLE));
            intent.putExtra(EXTRA_INDEX_OF, getIntent().getStringExtra(ARG_INDEX_OF));
            setResult(Activity.RESULT_FIRST_USER, intent);
            finish();
        } else {
            FragmentManager managerCancel = getSupportFragmentManager();
            BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                    getString(R.string.errorcreatingimage),
                    getString(R.string.sorryaboutthat),
                    getString(R.string.ok),
                    getResources().getString(R.string.dont_cancel), false);
            dialogCancel.show(managerCancel, DIALOG_NONE);
        }
    }

    private String takeMapSnapshot() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);
            return "";
        } else {
            final Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            try {
                final String[] mPath = {""};

                // Calculate ActionBar height
                mGMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    Bitmap bitmap;

                    @Override
                    public void onSnapshotReady(Bitmap snapshot) {
                        bitmap = snapshot;
                        mPath[0] = MediaStore.Images.Media.insertImage(
                                getContentResolver(),
                                bitmap,
                                now.toString(),
                                getString(R.string.gmapsscrnshot)
                        );

                        snapShotSend(mPath[0]);
                    }
                });
                return mPath[0];
            } catch (Throwable e) {
                e.printStackTrace();
                FragmentManager managerCancel = getSupportFragmentManager();
                BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                        getString(R.string.errorcreatingimage),
                        getString(R.string.sorryaboutthat),
                        getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                dialogCancel.show(managerCancel, DIALOG_NONE);
                return "";
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String filePath = takeMapSnapshot();
                    if (!filePath.equals("")) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_FILE_PATH, filePath);
                        setResult(Activity.RESULT_FIRST_USER, intent);
                        finish();
                    }
                }
                return;
            } default: {
                FragmentManager managerCancel = getSupportFragmentManager();
                BasicConfirmDialogFragment dialogCancel = BasicConfirmDialogFragment.newInstance(
                        getString(R.string.permissions), getString(R.string.permissions_warning),
                        getString(R.string.ok), getResources().getString(R.string.dont_cancel), false);
                dialogCancel.show(managerCancel, DIALOG_NONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
