package com.fyp.privacyguard.network

import com.fyp.privacyguard.network.ApiHelper.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {

    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
//        .client(httpClient)
        .build()

    var service: ApiService = retrofit.create(ApiService::class.java)

}