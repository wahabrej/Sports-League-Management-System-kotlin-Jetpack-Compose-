package com.example.myapplication.data.remote.api

import com.google.gson.annotations.SerializedName

data class CheckMeResponse(
    @SerializedName("authenticated") val authenticated: Boolean,
    @SerializedName("user") val user: User?
)