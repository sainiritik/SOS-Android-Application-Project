package com.example.sosapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

class SoSApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SOSTheme {
                EnhancedSoSScreen()
            }
        }
    }
}

@Composable
fun SOSTheme(content: @Composable () -> Unit) {
    val customColors = lightColorScheme(
        primary = Color(0xFF6A1B9A),
        onPrimary = Color.White,
        background = Color(0xFFF1F1F1),
        surface = Color.White,
        onSurface = Color.Black
    )

    val customTypography = Typography(
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
    )

    MaterialTheme(
        colorScheme = customColors,
        typography = customTypography,
        shapes = Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(12.dp),
            large = RoundedCornerShape(20.dp)
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedSoSScreen() {
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var sosNumber by remember { mutableStateOf(loadSOSNumber(context)) }
    var isShakeEnabled by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val denied = permissions.filterValues { !it }
        if (denied.isNotEmpty()) {
            Toast.makeText(context, "Some permissions were denied.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🚨 SOS App", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorScheme.primary)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        makePhoneCall(context, sosNumber)
                        sendSMSWithLocation(context, locationManager, sosNumber)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Send SOS", color = colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Shake Trigger", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = isShakeEnabled, onCheckedChange = { isShakeEnabled = it })
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF303F9F))
                ) {
                    Text("Edit SOS Number", color = colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        sosNumber = "7409556553"
                        saveSOSNumber(context, sosNumber)
                        Toast.makeText(context, "Default number restored", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restore Default")
                }

                if (showDialog) {
                    EditSOSDialog(
                        currentNumber = sosNumber,
                        onSubmit = {
                            sosNumber = it
                            saveSOSNumber(context, it)
                            showDialog = false
                        },
                        onDismiss = { showDialog = false }
                    )
                }
            }
        }
    )
}

@Composable
fun EditSOSDialog(
    currentNumber: String,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var input by remember { mutableStateOf(currentNumber) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { if (input.isNotBlank()) onSubmit(input) }
            ) { Text("Save") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Edit SOS Number") },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Phone Number") },
                singleLine = true
            )
        }
    )
}

// ----------------------
// 📲 Utility Functions
// ----------------------

fun makePhoneCall(context: Context, number: String) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
        == PackageManager.PERMISSION_GRANTED
    ) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number")
        context.startActivity(callIntent)
    } else {
        Toast.makeText(context, "Call permission not granted", Toast.LENGTH_SHORT).show()
    }
}

fun sendSMSWithLocation(context: Context, locationManager: LocationManager, number: String) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
    ) {
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            val msg = "SOS! Need help. Location: Lat=${location.latitude}, Lon=${location.longitude}"
            try {
                SmsManager.getDefault().sendTextMessage(number, null, msg, null, null)
                Toast.makeText(context, "SMS sent with location", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "SMS failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show()
    }
}

fun loadSOSNumber(context: Context): String {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return prefs.getString("SOSNumber", "7409556553") ?: "7409556553"
}

fun saveSOSNumber(context: Context, number: String) {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    prefs.edit().putString("SOSNumber", number).apply()
}
