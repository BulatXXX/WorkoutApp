package com.singularity.trainingapp.features.auth.data.signin

sealed interface SignInIntent{
    data object SignIn : SignInIntent
    data class ChangeEmail(val email: String) : SignInIntent
    data class ChangePassword(val password: String) : SignInIntent
    data object SwitchPasswordVisibility: SignInIntent
}
