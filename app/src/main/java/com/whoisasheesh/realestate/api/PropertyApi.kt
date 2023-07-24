package com.whoisasheesh.realestate.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object PropertyApi {
    const val propertiesApi = "properties"

    val apiService: PropertyApiService by lazy {
        val host = "https://f213b61d-6411-4018-a178-53863ed9f8ec.mock.pstmn.io/"
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder().baseUrl(host).client(client).build()
        retrofit.create(PropertyApiService::class.java)
    }
}