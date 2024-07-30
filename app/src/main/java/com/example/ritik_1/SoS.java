package com.example.ritik_1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.clans.fab.FloatingActionButton;

public class SoS extends AppCompatActivity implements LocationListener {

    public static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    public static final int REQUEST_LOCATION_PERMISSION = 2;
    public ImageButton sosButton;
    public FloatingActionButton sosEditbtn, sosRestorebtn;
    private ShakeDetector shakeDetector;
    private LocationManager locationManager;
    private String sosNumber;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String SOS_NUMBER_KEY = "SOSNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        // Initialize SOS Button
        sosButton = findViewById(R.id.sos_button);
        sosEditbtn = findViewById(R.id.edit_sosno);
        sosRestorebtn = findViewById(R.id.restore_sosno);

        // Initialize Shake Detector
        shakeDetector = new ShakeDetector(this);
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                makePhoneCall();
                sendSMSWithLocation();
            }
        });

        // Initialize Location Manager and check location permission
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkLocationPermission();

        // Load SOS number from SharedPreferences
        loadSOSNumber();

        // Set click listeners
        sosEditbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditSosNumberDialog();
            }
        });

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
                sendSMSWithLocation();
            }
        });

        sosRestorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restore default SOS number
                sosNumber = getDefaultSOSNumber();
                Toast.makeText(SoS.this, "SOS number restored successfully!", Toast.LENGTH_SHORT).show();
                // Save default SOS number to SharedPreferences
                saveDefaultSOSNumber(sosNumber);
            }
        });

        // Set listener for SOS Shake switch
        androidx.appcompat.widget.SwitchCompat SosShake = findViewById(R.id.sosshake);
        SosShake.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                // Save SOS Shake state to SharedPreferences
                saveSosShakeState(isChecked);

                if (isChecked) {
                    // If SOS Shake is enabled, register shake detector
                    shakeDetector.registerListener();
                } else {
                    // If SOS Shake is disabled, unregister shake detector
                    shakeDetector.unregisterListener();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeDetector.registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.unregisterListener();
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(SoS.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(SoS.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SoS.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, REQUEST_CALL_PHONE_PERMISSION);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + sosNumber));
            startActivity(callIntent);
        }
    }

    private void sendSMSWithLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();
                String message = "This is an SOS message. Please help! My current location is: Latitude " + latitude + ", Longitude " + longitude;

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sosNumber, null, message, null, null);
                    Toast.makeText(this, "SOS message with location sent successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to send SOS message with location. Make sure SMS permission is granted.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Failed to get current location. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            boolean callPermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean smsPermissionGranted = grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (callPermissionGranted && smsPermissionGranted) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission denied. Cannot make phone calls or send SMS.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMSWithLocation();
            } else {
                Toast.makeText(this, "Location permission denied. Cannot send SOS message with location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Not used here
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Not used here
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        // Not used here
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // Not used here
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    private void showEditSosNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_sos_number, null);
        final EditText sosNumberEditText = dialogView.findViewById(R.id.edit_sos_number);
        builder.setView(dialogView)
                .setTitle("Edit SOS Number")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newSosNumber = sosNumberEditText.getText().toString().trim();
                        if (!newSosNumber.isEmpty()) {
                            sosNumber = newSosNumber;
                            Toast.makeText(SoS.this, "SOS number updated successfully!", Toast.LENGTH_SHORT).show();
                            // Save new SOS number as default
                            saveDefaultSOSNumber(newSosNumber);
                        } else {
                            Toast.makeText(SoS.this, "Please enter a valid SOS number.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Unregister shake detector when dialog is shown
        shakeDetector.unregisterListener();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Register shake detector when dialog is dismissed
                shakeDetector.registerListener();
            }
        });
    }

    // Save default SOS number to SharedPreferences
    private void saveDefaultSOSNumber(String sosNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SOS_NUMBER_KEY, sosNumber);
        editor.apply();
    }

    // Load default SOS number from SharedPreferences
    private void loadSOSNumber() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        sosNumber = sharedPreferences.getString(SOS_NUMBER_KEY, ""); // Load saved SOS number
        if (sosNumber.isEmpty()) {
            sosNumber = "7409556553"; // Default SOS number
        }
    }

    // Get default SOS number
    private String getDefaultSOSNumber() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(SOS_NUMBER_KEY, "7409556553"); // Default SOS number
    }

    // Save SOS Shake state to SharedPreferences
    private void saveSosShakeState(boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sos_shake_state", isChecked);
        editor.apply();
    }
}
