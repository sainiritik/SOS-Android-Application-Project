package com.example.ritik_1.logic

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ritik_1.ui.MainActivityScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivityTest : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        if (user == null) {
            startActivity(Intent(this, LoginTest::class.java))
            finish()
        } else {
            setContent {
                MainActivityScreen(user!!)
            }
        }
    }
}
