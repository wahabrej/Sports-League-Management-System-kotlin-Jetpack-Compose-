package com.example.myapplication.data.remote.api

import OtpRequest
import ResetPasswordRequest
import com.example.myapplication.data.remote.models.requests.UpdateProfileRequest
import com.example.myapplication.data.remote.models.responses.UpdateProfileResponse
import com.example.myapplication.view.screen.home.MatchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST(ApiEndPoints.REGISTER)
    suspend fun register(@Body request: SignupRequest): Response<Unit>
    @POST(ApiEndPoints.SENT_OTP_EMAIL)
    suspend fun sentOtpEmail(@Body request: OtpRequest): Response<Unit>

    @PATCH(ApiEndPoints.RESET_PASSWORD)
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>
    @POST(ApiEndPoints.LOGIN)
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET(ApiEndPoints.CHECK_ME)
    suspend fun checkMe(
        @Header("Authorization") token: String
    ): Response<CheckMeResponse>

    @GET(ApiEndPoints.HOME_DATA)
    suspend fun getHomeData(
        @Header("Authorization") token: String
    ): Response<HomeDataResponse>
    @GET(ApiEndPoints.ALL_MATCHES)
    suspend fun getAllMatches(
        @Header("Authorization") token: String
    ):Response<List<MatchResponse>> // যেহেতু JSON একটি Array [] দিয়ে শুরু হয়েছে
    @GET(ApiEndPoints.ALL_MATCH)
    suspend fun getAllMatche(@Header("Authorization") token: String): Response<AllMatchResponse>

    @GET("api/match/one-match-details/{id}")
    suspend fun getSingleMatchDetails(
        @Header("Authorization") token: String,
        @Path("id") matchId: String
    ): Response<MatchDetailResponse>
    @PUT("api/users/update-profile/{userId}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>
}