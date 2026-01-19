package com.example.myapplication.data.remote.models.responses

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("_id") val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val first_name: String,
    val last_name: String,
    val role: String,
    val birth_date: String,
    val avatar: String?,
    val city: String?,
    val bio: String?,
    val updatedAt: String,
    @SerializedName("__v") val version: Int
)