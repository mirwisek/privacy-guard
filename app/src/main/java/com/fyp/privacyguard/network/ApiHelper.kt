package com.fyp.privacyguard.network

import com.fyp.privacyguard.data.model.LoggedInUser
import com.fyp.privacyguard.log
import com.fyp.privacyguard.network.model.ApiResult
import com.fyp.privacyguard.network.model.ApiUserResult
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ApiHelper {

    private val service = RetrofitBuilder.service

    const val BASE_URL = "http://192.168.10.5:5000/"
//    const val BASE_URL = "http://127.0.0.1:5000/"

    fun signup(user: LoggedInUser, onResult: (Result<ApiResult>) -> Unit) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", user.name!!)
            .addFormDataPart("email", user.email!!)
            .addFormDataPart("phone", user.phone!!)
            .addFormDataPart("password", user.password!!)
            .build()

        service.signup(requestBody).enqueue(object: Callback<ApiResult> {
            override fun onResponse(call: Call<ApiResult>, response: Response<ApiResult>) {
                val body = response.body()
                if(response.code() == 201 && body != null) {
                    onResult(Result.success(body))
                } else if(response.code() == 202 && body != null) { // On Duplicate, is not error
                    onResult(Result.failure(Exception(body.error)))
                } else {
                    // body will be null when status code is an error type
                    val errorBody = Gson().fromJson<ApiResult>(
                        response.errorBody()?.charStream(), ApiResult::class.java)
                    onResult(Result.failure(Exception(errorBody.error)))
                }
            }

            override fun onFailure(call: Call<ApiResult>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    fun login(user: LoggedInUser, onResult: (Result<LoggedInUser>) -> Unit) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM).apply {
                addFormDataPart("email", user.email!!)
                addFormDataPart("password", user.password!!)
            }.build()

        service.login(requestBody).enqueue(object: Callback<ApiUserResult> {
            override fun onResponse(call: Call<ApiUserResult>, response: Response<ApiUserResult>) {
                val body = response.body()
                if(response.isSuccessful && body != null) {
                    onResult(Result.success(body.result))
                } else {
                    // body will be null when status code is an error type
                    val errorBody = Gson().fromJson<ApiUserResult>(
                        response.errorBody()?.charStream(), ApiUserResult::class.java)
                    onResult(Result.failure(Exception(errorBody.error)))
                }
            }

            override fun onFailure(call: Call<ApiUserResult>, t: Throwable) {
                log("On failure called ${t.message}")
                onResult(Result.failure(t))
            }

        })
    }

    fun forgetPassword(email: String, onResult: (Result<String>) -> Unit) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM).apply {
                addFormDataPart("email", email)
            }.build()

        service.forgetPassword(requestBody).enqueue(object: Callback<ApiResult> {
            override fun onResponse(call: Call<ApiResult>, response: Response<ApiResult>) {
                val body = response.body()
                if(response.code() == 201 && body != null) {
                    onResult(Result.success(body.result))
                } else {
                    // body will be null when status code is an error type
                    val errorBody = Gson().fromJson<ApiResult>(
                        response.errorBody()?.charStream(), ApiResult::class.java)
                    onResult(Result.failure(Exception(errorBody.error)))
                }
            }

            override fun onFailure(call: Call<ApiResult>, t: Throwable) {
                onResult(Result.failure(t))
            }

        })
    }
}