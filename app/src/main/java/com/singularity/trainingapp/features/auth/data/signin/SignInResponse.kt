package com.singularity.trainingapp.features.auth.data.signin

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(val accessToken: String, val refreshToken: String)