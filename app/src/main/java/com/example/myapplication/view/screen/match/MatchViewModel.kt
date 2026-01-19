package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.PrefsManager
import com.example.myapplication.data.remote.api.MatchDetails
import com.example.myapplication.data.remote.api.MatchDetailsInfo
import com.example.myapplication.data.remote.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MatchViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = PrefsManager(application)

    // সব ম্যাচের লিস্ট স্টেট
    private val _matches = MutableStateFlow<List<MatchDetails>>(emptyList())
    val matches = _matches.asStateFlow()

    // সিঙ্গেল ম্যাচ ডিটেইলস স্টেট
    private val _matchDetail = MutableStateFlow<MatchDetailsInfo?>(null)
    val matchDetail = _matchDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        fetchAllMatches()
    }

    fun fetchAllMatches() {
        val token = prefsManager.getToken()
        if (token.isNullOrEmpty()) {
            _errorMessage.value = "User not authenticated"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = RetrofitClient.api.getAllMatche("Bearer $token")
                if (response.isSuccessful) {
                    // All matches API রেসপন্স থেকে লিস্ট নেওয়া
                    _matches.value = response.body()?.matches ?: emptyList()
                } else {
                    _errorMessage.value = "Failed to load matches: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * নির্দিষ্ট একটি ম্যাচের ডিটেইলস আনার ফাংশন
     */
    fun fetchMatchDetails(id: String) {
        val token = prefsManager.getToken()
        if (token.isNullOrEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            // নতুন ডাটা লোড হওয়ার আগে আগের স্টেট ক্লিয়ার করা ভালো
            _matchDetail.value = null

            try {
                val response = RetrofitClient.api.getSingleMatchDetails("Bearer $token", id)

                if (response.isSuccessful && response.body() != null) {
                    // MatchDetailResponse (Object) -> match (Nested Object)
                    _matchDetail.value = response.body()?.match
                } else {
                    _errorMessage.value = "Detail load failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        fetchAllMatches()
    }

    fun clearDetailState() {
        _matchDetail.value = null
    }
}