package com.example.myapplication.data.remote.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("user") val user: User? = null,
    @SerializedName("token") val token: String? = null
)

data class User(
    @SerializedName("_id") val sId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("childlist") val childlist: List<Any>? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("Apparment") val apparment: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("zip") val zip: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("isVerified") val isVerified: Boolean? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("rating") val rating: Int? = null,
    @SerializedName("reliability") val reliability: Int? = null,
    @SerializedName("total_job") val totalJob: Int? = null,
    @SerializedName("topbadge") val topbadge: Boolean? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("blacklist") val blacklist: Boolean? = null,
    @SerializedName("active") val active: Boolean? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null,
    @SerializedName("__v") val iV: Int? = null
)