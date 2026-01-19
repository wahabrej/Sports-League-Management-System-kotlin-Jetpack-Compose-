package com.example.myapplication.view.screen.home

import com.google.gson.annotations.SerializedName

data class MatchResponse(
    @SerializedName("_id")
    val id: String,

    @SerializedName("teamA")
    val teamA: TeamInfo,

    @SerializedName("teamB")
    val teamB: TeamInfo,

    val date: String,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    val stadium: String,
    val manager: ManagerInfo,
    val matchStatus: String,
    val status: String,
    val stopwatchTime: String,
    val votingOpen: Boolean,

    // JSON-এ null থাকতে পারে এমন ফিল্ডগুলো
    val manOfTheMatch: String? = null,
    val createdAt: String,
    val updatedAt: String
)

data class TeamInfo(
    val name: String,
    val totalGoals: Int,
    @SerializedName("teamALogo")
    val teamALogo: String? = null,
    @SerializedName("teamBLogo")
    val teamBLogo: String? = null,
    val players: List<Any> = emptyList(),
    val goalScorers: List<Any> = emptyList()
)

data class ManagerInfo(
    val name: String,
    @SerializedName("userId")
    val userId: String, // আপনার JSON-এ userId এখন একটি সরাসরি String
    @SerializedName("_id")
    val id: String
)