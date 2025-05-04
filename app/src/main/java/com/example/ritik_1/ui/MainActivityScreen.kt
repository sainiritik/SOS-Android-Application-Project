package com.example.ritik_1.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ritik_1.*
import com.example.ritik_1.R
import com.example.ritik_1.logic.LoginTest
import com.example.sosapp.SoSApp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainActivityScreen(user: com.google.firebase.auth.FirebaseUser) {
    val context = LocalContext.current
    val userName = user.displayName ?: "User"
    val profileImage = user.photoUrl?.toString()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Welcome,",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                    Text(
                        text = userName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                profileImage?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable {
                                context.startActivity(Intent(context, UserProfileActivity::class.java))
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { DashboardCard("Rescue", R.drawable.ic_baseline_people_24) { context.startActivity(Intent(context, SelectGenderActivity::class.java)) } }
                    item { DashboardCard("Emergency", R.drawable.ic_baseline_error_24) { context.startActivity(Intent(context, EmergencyActivity::class.java)) } }
                    item { DashboardCard("SOS", R.drawable.ic_baseline_emergency_24) { context.startActivity(Intent(context, SoSApp::class.java)) } }
                    item { DashboardCard("Contacts", R.drawable.ic_baseline_contacts_24) { context.startActivity(Intent(context, Contacts::class.java)) } }
                    item { DashboardCard("Map", R.drawable.ic_baseline_location_24) { context.startActivity(Intent(context, NearbyPlacesActivity::class.java)) } }
                    item { DashboardCard("Logout", R.drawable.ic_baseline_logout_24) {
                        FirebaseAuth.getInstance().signOut()
                        context.startActivity(Intent(context, LoginTest::class.java))
                    } }
                }
            }
        }
    )
}

@Composable
fun DashboardCard(title: String, iconRes: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = rememberAsyncImagePainter(iconRes),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color.Unspecified
            )
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
