package com.example.myapplication.viewmodel

import OtpRequest
import ResetPasswordRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.remote.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOtpSent: Boolean = false,
    val isOtpVerified: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    // ১. ওটিপি পাঠানোর ফাংশন
    fun sendOtpEmail(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isOtpSent = false) }
            try {
                val response = RetrofitClient.api.sentOtpEmail(OtpRequest(email))
                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isOtpSent = true, email = email) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Error: User not found or invalid email") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Connection Error: ${e.localizedMessage}") }
            }
        }
    }

    // ২. পাসওয়ার্ড রিসেট করার ফাংশন
    fun resetPassword(newPassword: String, otpCode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = ResetPasswordRequest(
                    email = _uiState.value.email,
                    password = newPassword,
                    otp = otpCode
                )

                val response = RetrofitClient.api.resetPassword(request)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isOtpVerified = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Invalid OTP or Reset Failed") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun resetState() {
        _uiState.update { AuthUiState() }
    }
}