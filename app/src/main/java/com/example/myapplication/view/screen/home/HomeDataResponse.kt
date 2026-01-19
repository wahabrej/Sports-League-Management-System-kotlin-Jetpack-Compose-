package com.example.myapplication.data.remote.api

import com.google.gson.annotations.SerializedName

data class HomeDataResponse(
    @SerializedName("totalPlayer") val totalPlayer: Int = 0,
    @SerializedName("totalRegisterPlayer") val totalRegisterPlayer: Int = 0,
    @SerializedName("totalMatch") val totalMatch: Int = 0,
    @SerializedName("totalParents") val totalParents: Int = 0,
    @SerializedName("totalRegisterParents") val totalRegisterParents: Int = 0
)