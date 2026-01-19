package com.example.myapplication.ui.screens.match

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.data.remote.api.ApiEndPoints
import com.example.myapplication.data.remote.api.MatchDetails
import com.example.myapplication.viewmodel.MatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllMatchScreen(
    viewModel: MatchViewModel,
    onMatchClick: (String) -> Unit // নেভিগেশনের জন্য
) {
    val matches by viewModel.matches.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match Schedules", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF8F9FA))) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            else if (errorMessage != null) {
                // Error handling...
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(matches) { match ->
                        MatchItemCard(match, onClick = { onMatchClick(match.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun MatchItemCard(match: MatchDetails, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }, // ক্লিক যোগ করা হয়েছে
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "🏟 ${match.stadium}", fontSize = 12.sp, color = Color.Gray)
                StatusBadge(match.status)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                TeamSection(match.teamA.name, match.teamA.teamALogo)
                Text(text = "${match.teamA.totalGoals} - ${match.teamB.totalGoals}", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                TeamSection(match.teamB.name, match.teamB.teamBLogo)
            }
        }
    }
}

@Composable
fun TeamSection(name: String, logo: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(90.dp)) {
        AsyncImage(
            model = ApiEndPoints.imageUrl(logo ?: ""),
            contentDescription = name,
            modifier = Modifier.size(54.dp).clip(CircleShape).background(Color(0xFFEEEEEE)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
        )
        Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = if (status == "ENDED") Color(0xFFD32F2F) else Color(0xFF388E3C)
    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
        Text(text = status, color = color, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}