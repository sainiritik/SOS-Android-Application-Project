// LoginTest.kt - Kotlin Backend for Login
package com.example.ritik_1.logic

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ritik_1.ui.Registration
import com.example.ritik_1.ui.LoginScreen
import com.google.firebase.auth.FirebaseAuth

class LoginTest : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMainScreen()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            LoginScreen(
                onLoginClick = { email, password -> signInUser(email, password) },
                onRegisterClick = { goToRegisterScreen() },
                onForgotPasswordClick = { showToast("Forgot Password flow here") }
            )
        }
    }

    private fun signInUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter both email and password.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showToast("Login Successful")
                    goToMainScreen()
                } else {
                    showToast("Login Failed: ${task.exception?.message}")
                }
            }
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivityTest::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToRegisterScreen() {
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
