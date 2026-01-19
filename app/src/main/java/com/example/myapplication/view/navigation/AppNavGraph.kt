package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.screens.match.MatchDetailScreen
import com.example.myapplication.ui.screens.profile.EditProfileScreen
import com.example.myapplication.viewmodel.MatchViewModel
import com.example.myapplication.viewmodel.ProfileViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val matchViewModel: MatchViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel() // Shared ViewModel for Profile

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) { popUpTo(Routes.SPLASH) { inclusive = true } }
                },
                onNavigateToParent = {
                    navController.navigate(Routes.PARENT) { popUpTo(Routes.SPLASH) { inclusive = true } }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.PARENT) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onSignupClick = { navController.navigate(Routes.SIGNUP) }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(onSignupSuccess = { navController.popBackStack() })
        }

        composable(Routes.PARENT) {
            ParentScreen(
                rootNavController = navController, // মেইন কন্ট্রোলার পাস করা হলো
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToDetail = { matchId ->
                    navController.navigate(Routes.MATCH_DETAIL.replace("{matchId}", matchId))
                }
            )
        }

        // এডিট প্রোফাইল স্ক্রিন মেইন গ্রাফে থাকতে হবে
        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                viewModel = profileViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.MATCH_DETAIL,
            arguments = listOf(navArgument("matchId") { type = NavType.StringType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId") ?: ""
            MatchDetailScreen(
                matchId = matchId,
                viewModel = matchViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}