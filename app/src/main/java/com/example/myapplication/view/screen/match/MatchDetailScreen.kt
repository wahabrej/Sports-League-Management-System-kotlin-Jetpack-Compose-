package com.example.myapplication.ui.screens.match

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.data.remote.api.ApiEndPoints
import com.example.myapplication.data.remote.api.MatchDetailsInfo
import com.example.myapplication.viewmodel.MatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    matchId: String,
    viewModel: MatchViewModel,
    onBack: () -> Unit
) {
    val matchDetail by viewModel.matchDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // স্ক্রিন লোড হওয়ার সময় ডাটা ফেচ করবে
    LaunchedEffect(matchId) {
        viewModel.fetchMatchDetails(matchId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA)) // Light background
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                matchDetail?.let { match ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // ১. স্কোরবোর্ড কার্ড
                        ScoreBoardCard(match)

                        Spacer(modifier = Modifier.height(24.dp))

                        // ২. ম্যাচ ইনফরমেশন (স্টেডিয়াম ও সময়)
                        InfoSection(
                            title = "Match Information",
                            items = listOf(
                                InfoItem("Stadium", match.stadium, Icons.Default.LocationOn),
                               // InfoItem("Start Time", match.startTime, Icons.Default.Timer),
                                InfoItem("Status", match.status, Icons.Default.Person)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        InfoSection(
                            title = "Management",
                            items = listOf(
                                InfoItem("Manager", match.manager.name, Icons.Default.Person)
                            )
                        )

                        // ৪. ভোটিং বাটন
                        if (match.votingOpen) {
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = { /* Vote Action */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Vote for Man of the Match", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreBoardCard(match: MatchDetailsInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 30.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Team A - লোগো কী: teamALogo
            TeamDisplay(match.teamA.name, match.teamA.teamALogo)

            // Score & Timer
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${match.teamA.totalGoals} : ${match.teamB.totalGoals}",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color.Black.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = match.stopwatchTime,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Team B - লোগো কী: teamBLogo
            TeamDisplay(match.teamB.name, match.teamB.teamBLogo)
        }
    }
}

@Composable
fun TeamDisplay(name: String, logo: String?) {
    // ইমেজ পাথ সঠিকভাবে তৈরি করা
    val imageUrl = if (!logo.isNullOrEmpty()) {
        "https://mvpitch.com/uploads/match/$logo"
    } else {
        null
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = ApiEndPoints.imageUrl(logo ?: ""),
            contentDescription = name,
            modifier = Modifier.size(54.dp).clip(CircleShape).background(Color(0xFFEEEEEE)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            maxLines = 1
        )
    }
}

@Composable
fun InfoSection(title: String, items: List<InfoItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title.uppercase(),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    Icon(item.icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "${item.label}:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = item.value, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ডাটা ক্লাস
data class InfoItem(val label: String, val value: String, val icon: ImageVector)