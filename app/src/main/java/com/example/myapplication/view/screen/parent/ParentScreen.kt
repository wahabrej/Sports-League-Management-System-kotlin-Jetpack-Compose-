package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication.ui.navigation.*
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.screens.match.AllMatchScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.MatchViewModel
import com.example.myapplication.viewmodel.ProfileViewModel

@Composable
fun ParentScreen(
    rootNavController: NavHostController, // এটি মেইন NavHost থেকে আসবে
    onLogout: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val innerNavController = rememberNavController() // এটি শুধুমাত্র বটম ন্যাভ স্ক্রিনগুলোর জন্য
    val homeViewModel: HomeViewModel = viewModel()
    val matchViewModel: MatchViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.AllMatch,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            innerNavController.navigate(item.route) {
                                popUpTo(innerNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = innerNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onMatchClick = { matchId -> onNavigateToDetail(matchId) }
                )
            }
            composable(Routes.AllMATCHSCREEN) {
                AllMatchScreen(
                    viewModel = matchViewModel,
                    onMatchClick = { matchId -> onNavigateToDetail(matchId) }
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = onLogout,
                    onNavigateToEditProfile = {
                        // সমাধান: ইনার কন্ট্রোলারের বদলে মেইন (Root) কন্ট্রোলার ব্যবহার করুন
                        rootNavController.navigate(Routes.EDIT_PROFILE)
                    }
                )
            }
        }
    }
}