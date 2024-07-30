package com.example.ritik_1;

import android.app.Activity;
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

public class AddContact extends AppCompatActivity {

    ImageView uploadImage;
    Button save_contact;
    EditText contactname, contactnumber;
    Spinner contactrelationSpinner;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        uploadImage = findViewById(R.id.uploadImage);
        contactname = findViewById(R.id.contactname);
        contactnumber = findViewById(R.id.contactnumber);
        contactrelationSpinner = findViewById(R.id.contactrelation);
        save_contact = findViewById(R.id.save_contact);

        // Create a list of contact relation options
        String[] contactRelationOptions = {"MySelf", "Family", "Friend", "Colleague", "Others"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contactRelationOptions);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        contactrelationSpinner.setAdapter(adapter);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            assert data != null;
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(AddContact.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        save_contact.setOnClickListener(new View.OnClickListener() {
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
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a reference to the storage location for the user's contact pictures
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("users").child(uid).child("contactPictures")
                .child(Objects.requireNonNull(uri.getLastPathSegment()));

        // Display a progress dialog while the image is being uploaded
        AlertDialog.Builder builder = new AlertDialog.Builder(AddContact.this);
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
                        imageURL = uri.toString();

                        // Call the method to upload contact data to Firebase Realtime Database
                        uploadData(uid);

                        // Dismiss the progress dialog
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors while getting the download URL
                        dialog.dismiss();
                        Toast.makeText(AddContact.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors while uploading the image
                dialog.dismiss();
                Toast.makeText(AddContact.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to upload contact data to Firebase Realtime Database
    private void uploadData(String uid) {
        // Get references to the Firebase Database nodes
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        DatabaseReference contactsRef = userRef.child("contacts");

        // Create a unique key for the new contact
        String contactKey = contactsRef.push().getKey();

        // Format the contact number
        String formattedContactNumber = formatContactNumber(contactnumber.getText().toString().trim());
        if (formattedContactNumber != null) {

        // Create a ContactDetail object with the contact's information
        ContactDetail contactData = new ContactDetail(contactname.getText().toString(),
                formattedContactNumber, contactrelationSpinner.getSelectedItem().toString(), imageURL);

        // Save the contact data under the user's contacts node with the unique key
        contactsRef.child(contactKey).setValue(contactData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddContact.this, "Contact saved successfully", Toast.LENGTH_SHORT).show();
                    // Clear input fields after successful save
                    contactname.setText("");
                    contactnumber.setText("");
                } else {
                    Toast.makeText(AddContact.this, "Failed to save contact", Toast.LENGTH_SHORT).show();
                }
            }
        });

        } else {
            // Show error message for invalid contact number format
            Toast.makeText(AddContact.this, "Invalid contact number format. Please enter a 10-digit number or a number starting with +91.", Toast.LENGTH_SHORT).show();
        }
    }
}
