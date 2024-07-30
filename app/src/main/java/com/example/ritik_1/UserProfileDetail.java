package com.example.ritik_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfileDetail extends AppCompatActivity {

    ImageView detailUserImage;
    TextView detailUserName, detailUserNumber, detailUserEmail, detailUserAddress;
    FloatingActionButton EditUserbtn, DeleteUserbtn;
    String name, number, email, address, image = "", key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);

        detailUserImage = findViewById(R.id.detailuserImage);
        detailUserName = findViewById(R.id.detailuserName);
        detailUserNumber = findViewById(R.id.detailuserNumber);
        detailUserEmail = findViewById(R.id.detailuserEmail);
        detailUserAddress = findViewById(R.id.detailuserAddress);
        EditUserbtn = findViewById(R.id.edituserButton);
        DeleteUserbtn = findViewById(R.id.deleteuserButton);

        // Retrieve data passed from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("name");
            number = intent.getStringExtra("number");
            email = intent.getStringExtra("email");
            address = intent.getStringExtra("address");
            image = intent.getStringExtra("image");
            key = intent.getStringExtra("key"); // Assuming "key" is passed to identify the specific data in the database

            // Set data to respective views
            detailUserName.setText(name);
            detailUserNumber.setText(number);
            detailUserEmail.setText(email);
            detailUserAddress.setText(address);

            // Load image using Glide library
            Glide.with(this).load(image).into(detailUserImage);
        }

        EditUserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileDetail.this, UpdateUserProfile.class);
                startActivity(intent);
            }
        });

        DeleteUserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Profile")
                        .child(key);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(image);

                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserProfileDetail.this, "Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                                finish();
                            }
                        });
                    }
                });
            }
        });

    }
}
