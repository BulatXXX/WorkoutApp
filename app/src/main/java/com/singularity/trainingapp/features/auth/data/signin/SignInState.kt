package com.singularity.trainingapp.features.auth.data.signin

data class SignInState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)