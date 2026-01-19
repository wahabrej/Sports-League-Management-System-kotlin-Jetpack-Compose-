package com.example.myapplication.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.remote.api.RetrofitClient
import com.example.myapplication.data.remote.api.SignupRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

class SignupViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState = _uiState.asStateFlow()

    fun updateName(name: String) = _uiState.update { it.copy(name = name) }
    fun updateEmail(email: String) = _uiState.update { it.copy(email = email) }
    fun updatePhone(phone: String) = _uiState.update { it.copy(phone = phone) }
    fun updatePassword(password: String) = _uiState.update { it.copy(password = password) }
    fun updateConfirmPassword(cp: String) = _uiState.update { it.copy(confirmPassword = cp) }

    fun signup() {
        val current = _uiState.value
        Log.d(TAG, "Signup started for email: ${current.email}")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = SignupRequest(
                    name = current.name.trim(),
                    email = current.email.trim(),
                    password = current.password,
                    phone = current.phone
                )

                val response = RetrofitClient.api.register(request)

                if (response.isSuccessful) {
                    Log.d(TAG, "Signup Successful! Code: ${response.code()}")
                    _uiState.update { it.copy(isLoading = false, success = true) }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Server Error: $errorBody | Code: ${response.code()}")
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Error: ${response.code()}") }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Fatal Exception: ${e.message}")
                e.printStackTrace() // পুরো স্ট্যাক ট্রেস প্রিন্ট করবে
                _uiState.update { it.copy(isLoading = false, errorMessage = "Connection Failed!") }
            }
        }
    }
}