package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.PrefsManager
import com.example.myapplication.data.remote.api.LoginRequest
import com.example.myapplication.data.remote.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = PrefsManager(application)
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(email: String) = _uiState.update { it.copy(email = email) }
    fun updatePassword(password: String) = _uiState.update { it.copy(password = password) }

    fun login() {
        val current = _uiState.value
        if (current.email.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill all fields") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = LoginRequest(current.email.trim(), current.password)

                // response will now be of type Response<LoginResponse>
                val response = RetrofitClient.api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    // Now these references will be resolved
                    body.token?.let { prefsManager.saveToken(it) }
                    body.user?.let { prefsManager.saveUser(it) }

                    _uiState.update { it.copy(isLoading = false, success = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Login Failed: ${response.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Check internet connection") }
            }
        }
    }
}