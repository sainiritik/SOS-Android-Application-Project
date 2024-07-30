package com.example.ritik_1;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class UpdateContact extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateNumber, updateName;
    Spinner updateRelationSpinner;

    String imageUrl;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        updateButton = findViewById(R.id.updateButton);
        updateNumber = findViewById(R.id.updateNumber);
        updateImage = findViewById(R.id.updateImage);
        updateRelationSpinner = findViewById(R.id.updateRelation);
        updateName = findViewById(R.id.updateName);

        // Create a list of contact relation options
        String[] contactRelationOptions = {"MySelf", "Family", "Friend", "Colleague", "Others"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contactRelationOptions);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        updateRelationSpinner.setAdapter(adapter);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK){
                            Intent data = result.getData();
                            assert data != null;
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateContact.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(UpdateContact.this).load(bundle.getString("Image")).into(updateImage);
            updateName.setText(bundle.getString("Name"));
            updateNumber.setText(bundle.getString("Number"));
            int position = adapter.getPosition(bundle.getString("Relation"));
            updateRelationSpinner.setSelection(position);
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private String formatContactNumber(String contactNumber) {
        if (contactNumber.length() == 10 && contactNumber.matches("\\d+")) {
            // If the number is 10 digits and only contains digits, add +91 prefix
            return "+91" + contactNumber;
        } else if (contactNumber.length() == 13 && contactNumber.startsWith("+91") && contactNumber.substring(3).matches("\\d+")) {
            // If the number is already in the format "+91XXXXXXXXXX", return it as is
            return contactNumber;
        } else {
            // Otherwise, return null to indicate invalid number format
            return null;
        }
    }

    public void saveData(){

        // Get the user's UID
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Create a reference to the storage location for the user's contact pictures
        storageReference = FirebaseStorage.getInstance().getReference()
                .child("users").child(uid).child("contactPictures")
                .child(Objects.requireNonNull(uri.getLastPathSegment()));

        // Display a progress dialog while the image is being uploaded
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateContact.this);
        builder.setCancelable(false);
        builder.setView(R.layout.activity_progressbar);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Upload the image to Firebase Storage
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Once the image is uploaded, get its download URL
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Convert the Uri to a String and store it as imageURL
                        imageUrl = uri.toString();

                        // Call the method to update contact data in Firebase Realtime Database
                        updateData(uid);

                        // Dismiss the progress dialog
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors while getting the download URL
                        dialog.dismiss();
                        Toast.makeText(UpdateContact.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors while uploading the image
                dialog.dismiss();
                Toast.makeText(UpdateContact.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update contact data in Firebase Realtime Database
    private void updateData(String uid) {
        // Get a reference to the Firebase Database node for the user's contacts
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("contacts").child(key);

        // Format the contact number
        String formattedContactNumber = formatContactNumber(updateNumber.getText().toString().trim());
        if (formattedContactNumber != null) {

        // Create a ContactDetail object with the updated contact's information
        ContactDetail updatedContact = new ContactDetail(updateName.getText().toString(),
                formattedContactNumber, updateRelationSpinner.getSelectedItem().toString(), imageUrl);

        // Update the contact data in the database
        databaseReference.setValue(updatedContact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // If the update is successful, delete the old image from Firebase Storage
                    StorageReference oldImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    oldImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Show a success message
                            Toast.makeText(UpdateContact.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                            // Finish the activity
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // If there's an error deleting the old image, show an error message
                            Toast.makeText(UpdateContact.this, "Failed to update contact", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // If the update fails, show an error message
                    Toast.makeText(UpdateContact.this, "Failed to update contact", Toast.LENGTH_SHORT).show();
                }
            }
        });

        } else {
            // Show error message for invalid contact number format
            Toast.makeText(UpdateContact.this, "Invalid contact number format. Please enter a 10-digit number or a number starting with +91.", Toast.LENGTH_SHORT).show();
        }
    }
}
