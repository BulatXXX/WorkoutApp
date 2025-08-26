package com.singularity.trainingapp.features.auth.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.singularity.trainingapp.features.auth.data.signin.SignInIntent
import com.singularity.trainingapp.features.auth.data.signin.SignInRequest
import com.singularity.trainingapp.features.auth.data.signin.SignInResponse
import com.singularity.trainingapp.features.auth.data.signin.SignInState
import com.singularity.trainingapp.core.MVIViewModel
import com.singularity.trainingapp.core.network.authApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignInViewModel : MVIViewModel<SignInIntent, SignInState, Nothing>() {

    override fun setInitialState(): SignInState = SignInState()

    override fun handleIntent(intent: SignInIntent) = when (intent) {
        is SignInIntent.ChangeEmail -> setState { uiState.value.copy(email = intent.email) }
        is SignInIntent.ChangePassword -> setState { uiState.value.copy(password = intent.password) }
        SignInIntent.SignIn -> {
            setState {
                uiState.value.copy(isLoading = true)
            }
            Log.i("HTTP_API", "email: ${uiState.value.email}, password: ${uiState.value.password}")
            val e = viewModelScope.launch(Dispatchers.IO) {
                try {
                    val resp: SignInResponse? =
                        authApi.login(SignInRequest(uiState.value.email, uiState.value.password)).body()
                    Log.i("HTTP_API", resp?.accessToken.toString())
                } catch (e: HttpException) {
                    Log.e("HTTP_API", e.message.toString())
                }

            }

        }

        SignInIntent.SwitchPasswordVisibility -> setState { uiState.value.copy(passwordVisible = !passwordVisible) }
    }
}