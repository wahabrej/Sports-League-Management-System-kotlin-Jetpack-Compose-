package com.example.myapplication.data.local

import android.content.Context
import com.example.myapplication.data.remote.api.User
import com.google.gson.Gson

class PrefsManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("auth_token", null)

    fun saveUser(user: User?) {
        val userJson = gson.toJson(user)
        prefs.edit().putString("user_data", userJson).apply()
    }

    fun getUser(): User? {
        val userJson = prefs.getString("user_data", null) ?: return null
        return try {
            gson.fromJson(userJson, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}