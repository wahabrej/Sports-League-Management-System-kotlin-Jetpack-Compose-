package com.example.myapplication.data.remote.api

import com.google.gson.annotations.SerializedName

// ১. মেইন রেসপন্স অবজেক্ট যা এপিআই থেকে সরাসরি আসবে
data class MatchDetailResponse(
    @SerializedName("match")
    val match: MatchDetailsInfo
)

// ২. ম্যাচের বিস্তারিত তথ্যের মডেল
data class MatchDetailsInfo(
    @SerializedName("_id")
    val id: String,
    val teamA: TeamDetails,
    val teamB: TeamDetails,
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
    val manOfTheMatch: String? = null,
    val createdAt: String,
    val updatedAt: String
)

// ৩. টিমের তথ্যের জন্য মডেল (টিম A এবং B উভয়ের জন্যই কাজ করবে)
data class TeamDetails(
    val name: String,
    val totalGoals: Int,
    @SerializedName("teamALogo") // টিম এ এর লোগোর জন্য
    val teamALogo: String? = null,
    @SerializedName("teamBLogo") // টিম বি এর লোগোর জন্য
    val teamBLogo: String? = null,
    val players: List<Any> = emptyList(), // JSON এ খালি অ্যারে আসছে
    val goalScorers: List<Any> = emptyList()
)

// ৪. ম্যানেজারের তথ্যের জন্য মডেল
data class ManagerInfo(
    @SerializedName("_id")
    val id: String,
    val name: String,
    @SerializedName("userId")
    val userDetails: UserDetails // এটি এখন আপনার JSON অনুযায়ী অবজেক্ট
)

// ৫. ম্যানেজারের ইউজার ডিটেইলস অবজেক্ট
data class UserDetails(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val avatar: String? = null
)