package com.example.mpesaStkPush.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AccessToken(
    @field:Expose @field:SerializedName("access_token") var accessToken: String,
    @field:Expose @field:SerializedName("expires_in") private val expiresIn: String
)
