package com.example.myapplication.data.remote.api

object ApiEndPoints {
    const val BASE_URL = "https://mvpitch.com/"

    // এন্ডপয়েন্টসমূহ
    const val LOGIN = "api/users/login"
    const val REGISTER = "api/users/register"
    const val CHECK_ME = "api/users/check"
    const val HOME_DATA = "api/admin/home-data"
    const val ALL_MATCHES = "api/admin/all-matches"
    const val ALL_MATCH = "api/match/all-match"
    const val SENT_OTP_EMAIL = "api/users/request-forgot-password-otp"
    const val RESET_PASSWORD = "api/users/reset-forgot-password"
//    fun matchDetail(id: String): String = "api/match/one-match-details/$id"
    // ইমেজ বা ফাইল দেখার জন্য ফুল ইউআরএল জেনারেটর
    fun imageUrl(imageName: String): String = "${BASE_URL}uploads/images/$imageName"
    fun fileUrl(fileName: String): String = "${BASE_URL}uploads/files/$fileName"
}