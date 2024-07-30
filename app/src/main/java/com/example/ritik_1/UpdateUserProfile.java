package com.example.ritik_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserProfile extends AppCompatActivity {
    EditText updateUserName, updateUserContactNumber, updateUserContactEmail, updateAddress;
    Button updateButton;
    DatabaseReference databaseReference;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        updateUserName = findViewById(R.id.updateusername);
        updateUserContactNumber = findViewById(R.id.updateusercontactnumber);
        updateUserContactEmail = findViewById(R.id.updateusercontactemail);
        updateAddress = findViewById(R.id.updateaddress);
        updateButton = findViewById(R.id.update_user);

        // Get the key of the user to update from the intent
        key = getIntent().getStringExtra("key");

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(key);

        // Retrieve existing data and set it to EditText fields for editing
        String name = getIntent().getStringExtra("name");
        String contactNumber = getIntent().getStringExtra("number");
        String email = getIntent().getStringExtra("email");
        String address = getIntent().getStringExtra("address");

        updateUserName.setText(name);
        updateUserContactNumber.setText(contactNumber);
        updateUserContactEmail.setText(email);
        updateAddress.setText(address);

        // Set OnClickListener for the update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve updated data from EditText fields
                String updatedName = updateUserName.getText().toString().trim();
                String updatedContactNumber = updateUserContactNumber.getText().toString().trim();
                String updatedEmail = updateUserContactEmail.getText().toString().trim();
                String updatedAddress = updateAddress.getText().toString().trim();

                // Update data in Firebase database
                updateUserData(updatedName, updatedContactNumber, updatedEmail, updatedAddress);
            }
        });
    }

    private void updateUserData(String name, String contactNumber, String email, String address) {
        // Update data in Firebase database
        databaseReference.child("name").setValue(name);
        databaseReference.child("phone").setValue(contactNumber);
        databaseReference.child("email").setValue(email);
        databaseReference.child("address").setValue(address);

        // Finish the activity after updating the data
        finish();
    }
}
