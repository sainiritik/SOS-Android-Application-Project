package com.example.ritik_1.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ritik_1.*
import com.example.ritik_1.R
import com.example.ritik_1.logic.LoginTest
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(user: com.google.firebase.auth.FirebaseUser) {
    val context = LocalContext.current
    val userName = user.displayName ?: "User"
    val profileImage = user.photoUrl?.toString()

    Scaffold(
        topBar = { MainTopBar(userName = userName, profileImage = profileImage) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add a quick action */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Action")
            }
        },
        content = { padding ->
            MainContent(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                onCardClick = { action ->
                    handleCardClick(context, action)
                }
            )
        }
    )
}

@Composable
fun MainTopBar(userName: String, profileImage: String?) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome back,",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
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
}

@Composable
fun MainContent(modifier: Modifier = Modifier, onCardClick: (DashboardAction) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(DashboardAction.values().size) { index ->
            val action = DashboardAction.values()[index]
            DashboardCard(
                title = action.title,
                iconRes = action.iconRes,
                onClick = { onCardClick(action) }
            )
        }
    }
}

@Composable
fun DashboardCard(title: String, iconRes: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

fun handleCardClick(context: android.content.Context, action: DashboardAction) {
    when (action) {
        DashboardAction.RESCUE -> context.startActivity(Intent(context, SelectGenderActivity::class.java))
        DashboardAction.EMERGENCY -> context.startActivity(Intent(context, EmergencyActivity::class.java))
        DashboardAction.SOS -> context.startActivity(Intent(context, SoS::class.java))
        DashboardAction.CONTACTS -> context.startActivity(Intent(context, Contacts::class.java))
        DashboardAction.MAP -> context.startActivity(Intent(context, NearbyPlacesActivity::class.java))
        DashboardAction.LOGOUT -> {
            FirebaseAuth.getInstance().signOut()
            context.startActivity(Intent(context, LoginTest::class.java))
        }
    }
}

enum class DashboardAction(val title: String, val iconRes: Int) {
    RESCUE("Rescue", R.drawable.ic_baseline_people_24),
    EMERGENCY("Emergency", R.drawable.ic_baseline_error_24),
    SOS("SOS", R.drawable.ic_baseline_emergency_24),
    CONTACTS("Contacts", R.drawable.ic_baseline_contacts_24),
    MAP("Map", R.drawable.ic_baseline_location_24),
    LOGOUT("Logout", R.drawable.ic_baseline_logout_24)
}