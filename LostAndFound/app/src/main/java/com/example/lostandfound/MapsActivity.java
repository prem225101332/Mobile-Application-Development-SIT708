package com.example.lostandfound;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedClient;
    private Location currentLocation;
    private SeekBar seekBar;
    private TextView tvRadius;
    private int radiusKm = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        seekBar = findViewById(R.id.seekBarRadius);
        tvRadius = findViewById(R.id.tvRadius);
        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        seekBar.setMax(50);
        seekBar.setProgress(10);
        tvRadius.setText("Radius: 10 km");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusKm = Math.max(1, progress);
                tvRadius.setText("Radius: " + radiusKm + " km");
                if (mMap != null && currentLocation != null) {
                    loadMarkers();
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        getCurrentLocationAndLoad();

        DatabaseHelper debugDb = new DatabaseHelper(this);
        List<LostFoundItem> debugItems = debugDb.getAllItems();
        for (LostFoundItem i : debugItems) {
            android.util.Log.d("MAPDBUG",
                    i.getName() + " lat=" + i.getLatitude() + " lng=" + i.getLongitude());
        }
    }

    private void getCurrentLocationAndLoad() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }
        mMap.setMyLocationEnabled(true);
        fusedClient.getLastLocation().addOnSuccessListener(location -> {
            LatLng target;
            if (location != null) {
                boolean inAustralia = location.getLatitude() >= -44
                        && location.getLatitude() <= -10
                        && location.getLongitude() >= 113
                        && location.getLongitude() <= 154;

                if (inAustralia) {
                    currentLocation = location;
                    target = new LatLng(location.getLatitude(), location.getLongitude());
                } else {
                    Location melbourneLocation = new Location("default");
                    melbourneLocation.setLatitude(-37.8136);
                    melbourneLocation.setLongitude(144.9631);
                    currentLocation = melbourneLocation;
                    target = new LatLng(-37.8136, 144.9631);
                    Toast.makeText(this, "Using Melbourne as default location", Toast.LENGTH_SHORT).show();
                }
            } else {
                Location melbourneLocation = new Location("default");
                melbourneLocation.setLatitude(-37.8136);
                melbourneLocation.setLongitude(144.9631);
                currentLocation = melbourneLocation;
                target = new LatLng(-37.8136, 144.9631);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 12f));
            loadMarkers();
        });
    }

    private void loadMarkers() {
        mMap.clear();

        if (currentLocation == null) return;

        mMap.addCircle(new CircleOptions()
                .center(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .radius(radiusKm * 1000)
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(3));

        DatabaseHelper db = new DatabaseHelper(this);
        List<LostFoundItem> items = db.getAllItems();
        int count = 0;

        for (LostFoundItem item : items) {
            if (item.getLatitude() == 0.0 && item.getLongitude() == 0.0) {
                LatLng pos = new LatLng(-37.8136, 144.9631);
                float markerColor = item.getType().equals("Lost")
                        ? BitmapDescriptorFactory.HUE_RED
                        : BitmapDescriptorFactory.HUE_GREEN;
                mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(item.getName())
                        .snippet(item.getType() + " — " + item.getLocation())
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                count++;
                continue;
            }

            float[] results = new float[1];
            Location.distanceBetween(
                    currentLocation.getLatitude(), currentLocation.getLongitude(),
                    item.getLatitude(), item.getLongitude(), results);
            float distanceKm = results[0] / 1000f;

            if (distanceKm <= radiusKm) {
                LatLng pos = new LatLng(item.getLatitude(), item.getLongitude());
                float markerColor = item.getType().equals("Lost")
                        ? BitmapDescriptorFactory.HUE_RED
                        : BitmapDescriptorFactory.HUE_GREEN;
                mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(item.getName())
                        .snippet(item.getType() + " — " + item.getLocation())
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                count++;
            }
        }

        Toast.makeText(this, count + " items within " + radiusKm + " km", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocationAndLoad();
        }
    }
}
