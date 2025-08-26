package com.singularity.trainingapp.features.auth.data.signin

import kotlinx.serialization.Serializable

@Serializable data class SignInRequest(val email: String, val password: String)
