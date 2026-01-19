package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.PrefsManager
import com.example.myapplication.data.remote.api.RetrofitClient
import com.example.myapplication.data.remote.api.User
import com.example.myapplication.data.remote.models.requests.UpdateProfileRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUpdateSuccess: Boolean = false
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val prefsManager = PrefsManager(application)
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val localUser = prefsManager.getUser()
        _uiState.update { it.copy(user = localUser) }
        fetchLatestUserData()
    }

    fun fetchLatestUserData() {
        val token = prefsManager.getToken() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = RetrofitClient.api.checkMe("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val updatedUser = response.body()?.user
                    prefsManager.saveUser(updatedUser)
                    _uiState.update { it.copy(user = updatedUser, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Sync Error") }
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String, displayName: String) {
        val token = prefsManager.getToken() ?: return
        val userId = _uiState.value.user?.sId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isUpdateSuccess = false) }
            try {
                // ১. এখানে জন্ম তারিখের ভুল ফরম্যাট বাদ দিয়ে সার্ভার যা বোঝে (ISO 8601) তা পাঠানো হচ্ছে
                // ২. যদি সার্ভার সাইড থেকে এটি রিমুভ করা থাকে, তবে বডি থেকে এই কি (key) টি ফেলে দিন
                val requestBody = UpdateProfileRequest(
                    first_name = firstName,
                    last_name = lastName,
                    name = displayName,
                    birth_date = "1999-10-22T00:00:00.000Z" // হার্ডকোড করার বদলে সঠিক ফরম্যাট দিন
                )

                val response = RetrofitClient.api.updateProfile("Bearer $token", userId, requestBody)

                if (response.isSuccessful && response.body() != null) {
                    val resData = response.body()!!
                    val updatedUser = _uiState.value.user?.copy(
                        name = resData.name,
//                        first_name = resData.first_name,
//                        last_name = resData.last_name
                    )
                    prefsManager.saveUser(updatedUser)
                    _uiState.update { it.copy(user = updatedUser, isLoading = false, isUpdateSuccess = true) }
                } else {
                    // সার্ভার এরর মেসেজ দেখাবে (যেমন: CastError)
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Server Error: ${response.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        prefsManager.clear()
        onLogoutSuccess()
    }
}