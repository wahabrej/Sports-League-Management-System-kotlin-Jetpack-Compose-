package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Routes.HOME, "Home", Icons.Default.Home)
    // টাইটেল 'Favorite' থেকে পরিবর্তন করে 'Matches' করা হয়েছে এবং আইকন পরিবর্তন করা হয়েছে
    object AllMatch : BottomNavItem(Routes.AllMATCHSCREEN, "Matches", Icons.Default.DateRange)
    object Profile : BottomNavItem(Routes.PROFILE, "Profile", Icons.Default.Person)
}