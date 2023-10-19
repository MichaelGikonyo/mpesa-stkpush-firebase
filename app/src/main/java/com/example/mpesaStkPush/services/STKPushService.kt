package com.example.mpesaStkPush.services

import com.example.mpesaStkPush.model.AccessToken
import com.example.mpesaStkPush.model.STKPush
import com.example.mpesaStkPush.model.STKPushResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface STKPushService {
    @POST("mpesa/stkpush/v1/processrequest")
    fun sendPush(@Body stkPush: STKPush?): Call<STKPushResponse?>?

    @get:GET("oauth/v1/generate?grant_type=client_credentials")
    val accessToken: Call<AccessToken?>?
}