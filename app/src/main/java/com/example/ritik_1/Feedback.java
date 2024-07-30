package com.example.ritik_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends AppCompatActivity {

    private EditText editTextFeedback, editTextEmail;
    private Button btnSubmit, btnReset;
    private DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Change the color of the status bar and navigation bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.platinum));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.platinum));


        // Initialize Firebase Database reference
        feedbackRef = FirebaseDatabase.getInstance().getReference("feedback");

        // Initialize views
        editTextFeedback = findViewById(R.id.textFeedback);
        editTextEmail = findViewById(R.id.textEmail);
        btnSubmit = findViewById(R.id.feedbackSubmit);
        btnReset = findViewById(R.id.resetText);

        // Set onClickListener for the Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the feedback text
                String feedback = editTextFeedback.getText().toString().trim();
                // Get the email address
                String emailAddress = editTextEmail.getText().toString().trim();

                if (feedback.isEmpty()) {
                    // If feedback is empty, show a toast
                    Toast.makeText(Feedback.this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
                } else if (emailAddress.isEmpty()) {
                    // If email address is empty, show a toast
                    Toast.makeText(Feedback.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                } else {
                    // If feedback and email address are not empty, submit feedback
                    submitFeedback(feedback, emailAddress);
                }
            }
        });

        // Set onClickListener for the Reset button
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the editTextFeedback
                editTextFeedback.setText("");
                // Clear the editTextEmail
                editTextEmail.setText("");
            }
        });
    }

    private void submitFeedback(String feedback, String emailAddress) {
        // Get the current user's ID (assuming you are using Firebase Authentication)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Generate a unique key for the feedback
            String feedbackId = feedbackRef.push().getKey();

            // Create Feedback object
            FeedbackObject feedbackObject = new FeedbackObject(feedbackId, feedback, emailAddress);

            // Submit feedback to Firebase Realtime Database under the current user's node
            feedbackRef.child(userId).child(feedbackId).setValue(feedbackObject)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Feedback submitted successfully
                            Toast.makeText(Feedback.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to submit feedback
                            Toast.makeText(Feedback.this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User is not signed in
            Toast.makeText(Feedback.this, "User is not signed in", Toast.LENGTH_SHORT).show();
        }
    }

}
