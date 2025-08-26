package com.singularity.trainingapp.core.network

//class AuthInterceptor(
//    private val tokenProvider: () -> String?
//) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val req = chain.request()
//        val b = req.newBuilder()
//            .header("Accept", "application/json")
//        tokenProvider()?.let { b.header("Authorization", "Bearer $it") }
//        return chain.proceed(b.build())
//    }
//}
//
//fun loggingInterceptor(debug: Boolean): HttpLoggingInterceptor =
//    HttpLoggingInterceptor().apply {
//        level = if (debug) HttpLoggingInterceptor.Level.BODY
//        else HttpLoggingInterceptor.Level.BASIC
//    }