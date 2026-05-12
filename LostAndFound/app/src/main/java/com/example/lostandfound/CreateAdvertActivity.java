package com.example.lostandfound;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;
import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    private EditText etName, etPhone, etDesc, etDate, etLocation;
    private TextView tvLocationStatus;
    private RadioButton radioLost, radioFound;
    private Spinner spinnerCategory;
    private ImageView imagePreview;
    private String savedImagePath = "";
    private double selectedLat = 0.0, selectedLng = 0.0;
    private FusedLocationProviderClient fusedClient;
    private LocationCallback locationCallback;

    private final String[] categories = {
        "Electronics", "Pets", "Wallets", "Clothing",
        "Keys", "Bags", "Jewelry", "Documents", "Other"
    };

    private final ActivityResultLauncher<String> imagePickerLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                savedImagePath = copyUriToInternalStorage(uri);
                if (!savedImagePath.isEmpty()) {
                    imagePreview.setImageBitmap(BitmapFactory.decodeFile(savedImagePath));
                    imagePreview.setVisibility(View.VISIBLE);
                }
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        setTitle("Create New Advert");

        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        etName           = findViewById(R.id.etName);
        etPhone          = findViewById(R.id.etPhone);
        etDesc           = findViewById(R.id.etDescription);
        etDate           = findViewById(R.id.etDate);
        etLocation       = findViewById(R.id.etLocation);
        tvLocationStatus = findViewById(R.id.tvLocationStatus);
        radioLost        = findViewById(R.id.radioLost);
        radioFound       = findViewById(R.id.radioFound);
        spinnerCategory  = findViewById(R.id.spinnerCategory);
        imagePreview     = findViewById(R.id.imagePreview);

        // Category spinner
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        // Date picker
        etDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) ->
                etDate.setText(y + "-" + (m + 1) + "-" + d),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Search location button — converts typed address to coordinates
        findViewById(R.id.btnSearchLocation).setOnClickListener(v -> {
            String address = etLocation.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Please type a location first", Toast.LENGTH_SHORT).show();
                return;
            }
            searchLocation(address);
        });

        // Current location button
        findViewById(R.id.btnCurrentLocation).setOnClickListener(v -> getCurrentLocation());

        // Image picker
        findViewById(R.id.btnPickImage).setOnClickListener(v ->
            imagePickerLauncher.launch("image/*"));

        // Save
        findViewById(R.id.btnSave).setOnClickListener(v -> saveItem());
    }

    // ✅ Converts any typed address to lat/lng using Geocoder
    private void searchLocation(String addressText) {
        tvLocationStatus.setText("Searching...");
        tvLocationStatus.setTextColor(0xFF888888);

        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> results = geocoder.getFromLocationName(addressText, 1);

                runOnUiThread(() -> {
                    if (results != null && !results.isEmpty()) {
                        Address found = results.get(0);
                        selectedLat = found.getLatitude();
                        selectedLng = found.getLongitude();

                        // Show readable address back to user
                        String readable = found.getAddressLine(0);
                        etLocation.setText(readable);
                        tvLocationStatus.setText("✅ Found: " + String.format("%.4f", selectedLat)
                            + ", " + String.format("%.4f", selectedLng));
                        tvLocationStatus.setTextColor(0xFF3CAD6E);

                        Toast.makeText(this,
                            "Location set to: " + readable,
                            Toast.LENGTH_LONG).show();
                    } else {
                        tvLocationStatus.setText("❌ Address not found. Try being more specific.");
                        tvLocationStatus.setTextColor(0xFFE05555);
                        Toast.makeText(this,
                            "Could not find that address. Try adding city or country.",
                            Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    tvLocationStatus.setText("❌ Search failed. Check internet connection.");
                    tvLocationStatus.setTextColor(0xFFE05555);
                });
            }
        }).start();
    }

    // ✅ Gets device GPS location and reverse geocodes it to an address
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
            return;
        }

        tvLocationStatus.setText("Getting your location...");
        tvLocationStatus.setTextColor(0xFF888888);

        // Try getLastLocation first — faster and less resource intensive
        fusedClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                selectedLat = location.getLatitude();
                selectedLng = location.getLongitude();
                reverseGeocode(selectedLat, selectedLng);
            } else {
                // Fall back to requesting a fresh location
                requestFreshLocation();
            }
        }).addOnFailureListener(e -> {
            tvLocationStatus.setText("❌ Location failed. Type address manually.");
            tvLocationStatus.setTextColor(0xFFE05555);
        });
    }

    private void requestFreshLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;

        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMaxUpdates(1)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                // Always remove updates immediately to prevent crashes
                if (locationCallback != null) {
                    fusedClient.removeLocationUpdates(locationCallback);
                }
                Location loc = result.getLastLocation();
                if (loc != null) {
                    selectedLat = loc.getLatitude();
                    selectedLng = loc.getLongitude();
                    reverseGeocode(selectedLat, selectedLng);
                }
            }
        };

        fusedClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());

        // Auto-cancel after 10 seconds if no location received
        new android.os.Handler(getMainLooper()).postDelayed(() -> {
            if (locationCallback != null) {
                fusedClient.removeLocationUpdates(locationCallback);
                locationCallback = null;
                if (selectedLat == 0.0) {
                    tvLocationStatus.setText("❌ Could not get location. Type address manually.");
                    tvLocationStatus.setTextColor(0xFFE05555);
                }
            }
        }, 10000);
    }

    private void reverseGeocode(double lat, double lng) {
        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(CreateAdvertActivity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                runOnUiThread(() -> {
                    if (addresses != null && !addresses.isEmpty()) {
                        String addr = addresses.get(0).getAddressLine(0);
                        etLocation.setText(addr);
                    } else {
                        etLocation.setText("Lat: " + String.format("%.4f", lat)
                            + ", Lng: " + String.format("%.4f", lng));
                    }
                    tvLocationStatus.setText("✅ " + String.format("%.4f", lat)
                        + ", " + String.format("%.4f", lng));
                    tvLocationStatus.setTextColor(0xFF3CAD6E);
                    Toast.makeText(CreateAdvertActivity.this,
                        "Location captured!", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    etLocation.setText("Lat: " + String.format("%.4f", lat)
                        + ", Lng: " + String.format("%.4f", lng));
                    tvLocationStatus.setText("✅ Location captured");
                    tvLocationStatus.setTextColor(0xFF3CAD6E);
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedClient != null && locationCallback != null) {
            fusedClient.removeLocationUpdates(locationCallback);
            locationCallback = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }

    private String copyUriToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return "";
            File dir = new File(getFilesDir(), "images");
            if (!dir.exists()) dir.mkdirs();
            File outFile = new File(dir, "img_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(outFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
            return outFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void saveItem() {
        String name     = etName.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String desc     = etDesc.getText().toString().trim();
        String date     = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || desc.isEmpty()
                || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Warn if no coordinates set
        if (selectedLat == 0.0 && selectedLng == 0.0) {
            Toast.makeText(this,
                "Please search or get your location first!",
                Toast.LENGTH_LONG).show();
            return;
        }

        LostFoundItem item = new LostFoundItem();
        item.setType(radioLost.isChecked() ? "Lost" : "Found");
        item.setName(name);
        item.setPhone(phone);
        item.setDescription(desc);
        item.setDate(date);
        item.setLocation(location);
        item.setCategory(categories[spinnerCategory.getSelectedItemPosition()]);
        item.setImagePath(savedImagePath);
        item.setLatitude(selectedLat);
        item.setLongitude(selectedLng);

        new DatabaseHelper(this).insertItem(item);
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
