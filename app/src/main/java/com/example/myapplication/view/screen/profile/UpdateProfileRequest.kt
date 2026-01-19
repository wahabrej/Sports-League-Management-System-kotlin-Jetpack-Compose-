package com.example.myapplication.data.remote.models.requests

data class UpdateProfileRequest(
    val first_name: String,
    val last_name: String,
    val birth_date: String, // format: "1999-10-22T00:00:00.000Z"
    val name: String
)