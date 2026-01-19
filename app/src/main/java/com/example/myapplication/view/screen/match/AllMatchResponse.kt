package com.example.myapplication.data.remote.api

import com.example.myapplication.view.screen.home.TeamInfo
import com.google.gson.annotations.SerializedName

data class AllMatchResponse(
    @SerializedName("matches") val matches: List<MatchDetails>,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalMatches") val totalMatches: Int
)

data class MatchDetails(
    @SerializedName("_id") val id: String,
    @SerializedName("teamA") val teamA: TeamInfo,
    @SerializedName("teamB") val teamB: TeamInfo,
    @SerializedName("stadium") val stadium: String,
    @SerializedName("date") val date: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("stopwatchTime") val stopwatchTime: String
)

//data class TeamInfo(
//    @SerializedName("name") val name: String,
//    @SerializedName("totalGoals") val totalGoals: Int,
//    @SerializedName("teamALogo") val teamALogo: String? = null,
//    @SerializedName("teamBLogo") val teamBLogo: String? = null
//)