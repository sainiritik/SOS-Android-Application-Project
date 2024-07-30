package com.example.ritik_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    FloatingActionButton fabedit;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    CardView cardAbout;
    androidx.appcompat.widget.SwitchCompat Switchtheme;
    com.github.clans.fab.FloatingActionButton Aboutbutton, Feedbackbtn;
    List<UserProfileData> dataList;
    ProfileItem adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        recyclerView = findViewById(R.id.recyclerViewProfile);
        fabedit = findViewById(R.id.fabeditprofile);
        cardAbout = findViewById(R.id.recCardUser);
        Aboutbutton = findViewById(R.id.aboutButton);
        Feedbackbtn = findViewById(R.id.feedbackButton);
        Switchtheme = findViewById(R.id.switchTheme);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserProfileActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        /*AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.activity_progressbar_wait);
        AlertDialog dialog = builder.create();
        dialog.show();*/

        dataList = new ArrayList<>();

        adapter = new ProfileItem(UserProfileActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        // Get the currently logged-in user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("Profile");
            //dialog.show();
            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                        UserProfileData dataClass = itemSnapshot.getValue(UserProfileData.class);

                        assert dataClass != null;
                        dataClass.setKey(itemSnapshot.getKey());

                        dataList.add(dataClass);
                    }
                    adapter.notifyDataSetChanged();
                    //dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //dialog.dismiss();
                }
            });
        }


        fabedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        Feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, Feedback.class);
                startActivity(intent);
            }
        });

        /*cardAbout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, About.class);
                startActivity(intent);
            }
        });*/

        Aboutbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, About.class);
                startActivity(intent);
            }
        });

        // Set a listener for the theme switch
        Switchtheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Switch between light mode and dark mode based on the switch state
                if (isChecked) {
                    // Dark mode is enabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Light mode is enabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                // Recreate the activity to apply the new theme
                onRestart();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}
