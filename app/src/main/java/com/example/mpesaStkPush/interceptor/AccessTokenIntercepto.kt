package com.example.mpesaStkPush.interceptor

import android.util.Base64
import com.example.mpesaStkPush.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class AccessTokenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        BuildConfig.BUILD_TYPE
        val keys: String = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET
        val request: Request = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "Basic " + Base64.encodeToString(keys.toByteArray(), Base64.NO_WRAP)
            )
            .build()
        return chain.proceed(request)
    }
}