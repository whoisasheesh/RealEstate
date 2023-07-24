package com.whoisasheesh.realestate.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface PropertyApiService {
    @get:GET(PropertyApi.propertiesApi)
    val propertiesList: Call<ResponseBody>
}