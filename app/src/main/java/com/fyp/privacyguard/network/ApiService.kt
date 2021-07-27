package com.fyp.privacyguard.network

import com.fyp.privacyguard.network.model.ApiResult
import com.fyp.privacyguard.network.model.ApiUserResult
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/signup")
    fun signup(@Body body: RequestBody): Call<ApiResult>

    @POST("/login")
    fun login(@Body body: RequestBody): Call<ApiUserResult>

    @POST("/recovery")
    fun forgetPassword(@Body body: RequestBody): Call<ApiResult>

}