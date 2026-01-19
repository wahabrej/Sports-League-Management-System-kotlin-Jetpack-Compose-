package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.view.screen.home.MatchResponse
import com.example.myapplication.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onMatchClick: (String) -> Unit // নেভিগেশনের জন্য আইডি পাস করবে
) {
    val homeData by viewModel.homeData.collectAsState()
    val matches by viewModel.matches.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard", fontWeight = FontWeight.Bold) },
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
                .background(Color(0xFFF7F9FB)) // হালকা ব্যাকগ্রাউন্ড কালার
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // ১. হেডার সেকশন
                    item {
                        Column {
                            Text(
                                text = "Welcome Admin",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Statistics and recent match updates",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // ২. স্ট্যাটিস্টিকস কার্ড গ্রিড
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatGridCard(Modifier.weight(1f), "Total Players", "${homeData?.totalPlayer ?: 0}", Icons.Default.Person, Color(0xFF42A5F5))
                                StatGridCard(Modifier.weight(1f), "Matches", "${homeData?.totalMatch ?: 0}", Icons.Default.Star, Color(0xFFFFA726))
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatGridCard(Modifier.weight(1f), "Reg. Players", "${homeData?.totalRegisterPlayer ?: 0}", Icons.Default.CheckCircle, Color(0xFF66BB6A))
                                StatGridCard(Modifier.weight(1f), "Total Parents", "${homeData?.totalParents ?: 0}", Icons.Default.AccountBox, Color(0xFFAB47BC))
                            }
                        }
                    }

                    // ৩. ম্যাচের লিস্ট টাইটেল
                    item {
                        Text(
                            text = "Recent Matches",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // ৪. ডাইনামিক ম্যাচের লিস্ট
                    if (matches.isEmpty()) {
                        item {
                            Text(
                                "No matches found",
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    } else {
                        items(matches) { match ->
                            MatchCard(
                                match = match,
                                onClick = { onMatchClick(match.id) }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(10.dp)) }
                }
            }
        }
    }
}

@Composable
fun StatGridCard(modifier: Modifier, title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
            Column {
                Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text(title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun MatchCard(match: MatchResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Team A
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(match.teamA.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
                Text("${match.teamA.totalGoals}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            }

            // VS Section
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(0.6f)) {
                Text("VS", fontWeight = FontWeight.Black, color = Color.LightGray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                StatusTag(match.status)
            }

            // Team B
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(match.teamB.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
                Text("${match.teamB.totalGoals}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun StatusTag(status: String) {
    val isEnded = status.equals("ENDED", ignoreCase = true)
    val color = if (isEnded) Color(0xFFE57373) else Color(0xFF81C784)
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = status,
            color = if (isEnded) Color.Red else Color(0xFF2E7D32),
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontWeight = FontWeight.Bold
        )
    }
}