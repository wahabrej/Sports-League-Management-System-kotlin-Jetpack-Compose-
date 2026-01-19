package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.PrefsManager
import com.example.myapplication.data.remote.api.HomeDataResponse
import com.example.myapplication.view.screen.home.MatchResponse
import com.example.myapplication.data.remote.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val prefsManager = PrefsManager(application)

    // হোম স্ট্যাটিস্টিকস ডাটা
    private val _homeData = MutableStateFlow<HomeDataResponse?>(null)
    val homeData = _homeData.asStateFlow()

    // ম্যাচের লিস্ট ডাটা
    private val _matches = MutableStateFlow<List<MatchResponse>>(emptyList())
    val matches = _matches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        refreshDashboard()
    }

    // দুটি এপিআই একসাথে কল করার ফাংশন
    fun refreshDashboard() {
        val token = prefsManager.getToken() ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // ১. হোম ডাটা ফেচ করা
                val homeResponse = RetrofitClient.api.getHomeData("Bearer $token")
                if (homeResponse.isSuccessful) {
                    _homeData.value = homeResponse.body()
                }

                // ২. ম্যাচের লিস্ট ফেচ করা
                val matchResponse = RetrofitClient.api.getAllMatches("Bearer $token")
                if (matchResponse.isSuccessful) {
                    _matches.value = matchResponse.body() ?: emptyList()
                }

            } catch (e: Exception) {
                // এখানে এরর মেসেজ হ্যান্ডেল করতে পারেন
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}