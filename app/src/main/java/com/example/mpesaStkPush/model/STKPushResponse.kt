package com.example.mpesaStkPush.model

data class STKPushResponse(
    val CheckoutRequestID: String,
    val CustomerMessage: String,
    val MerchantRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String
)