package com.singularity.trainingapp.core.network

import com.singularity.trainingapp.features.auth.data.signin.SignInRequest
import com.singularity.trainingapp.features.auth.data.signin.SignInResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

private val BASE_URL = "http://10.0.2.2:8080/"

val okHttp = OkHttpClient.Builder()
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(20, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit
    .Builder()
    .addConverterFactory(JSONProvider.json.asConverterFactory("application/json".toMediaType()))
    .client(okHttp)
    .baseUrl(BASE_URL)
    .build()


interface AuthApi {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body body: SignInRequest): Response<SignInResponse>
}

val authApi = retrofit.create(AuthApi::class.java)


