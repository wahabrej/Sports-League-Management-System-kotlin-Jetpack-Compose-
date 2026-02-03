package com.example.myapplication.ui.screens

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.local.PrefsManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToParent: () -> Unit

) {
    val context = LocalContext.current
    val prefsManager = remember { PrefsManager(context) }

    LaunchedEffect(Unit) {

        delay(2000)
        if (prefsManager.getToken().isNullOrEmpty()) {
            onNavigateToLogin()
        } else {
            onNavigateToParent()

        }

    }
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,

            ) {
            Text(
                "My App",
                modifier = Modifier.background(Color.Transparent),
                color = Color.Green,

                )
        }
    }


}