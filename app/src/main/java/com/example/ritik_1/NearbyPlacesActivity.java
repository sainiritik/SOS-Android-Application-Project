package com.example.ritik_1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class NearbyPlacesActivity extends AppCompatActivity {

    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

        // Initialize the map view
        mapView = findViewById(R.id.mapView);
        Configuration.getInstance().load(this, getSharedPreferences("OSMDROID", MODE_PRIVATE));
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Initialize the map controller and set the initial map center
        IMapController mapController = mapView.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(29.867309, 77.955634); // Ghar, India
        mapController.setCenter(startPoint);

        // Enable the display of the user's location on the map
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);

        // Check for location permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Location permission granted, proceed with displaying the map
                mapView.onResume();
            }
        } else {
            // For devices below Marshmallow, location permission is granted in the manifest, so proceed with displaying the map
            mapView.onResume();
        }

        // Button to go to current location
        Button btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move the map to the current location
                GeoPoint currentLocation = myLocationOverlay.getMyLocation();
                if (currentLocation != null) {
                    mapView.getController().animateTo(currentLocation);
                } else {
                    Toast.makeText(NearbyPlacesActivity.this, "Current location not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permissions granted, proceed with displaying the map
                mapView.onResume();
            } else {
                // Location permissions denied, display a message and close the activity
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
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
    protected void onDestroy() {
        super.onDestroy();
        if (myLocationOverlay != null) {
            myLocationOverlay.disableMyLocation();
        }
        mapView.onDetach();
    }
}

class PermissionUtils {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
}
