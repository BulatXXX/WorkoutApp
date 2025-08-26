package com.singularity.trainingapp.features.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singularity.trainingapp.R
import com.singularity.trainingapp.features.auth.data.signin.SignInIntent

@Composable
fun SignInScreen(viewModel: SignInViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Welcome Back!", style = TextStyle(fontSize = 30.sp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                singleLine = true,
                onValueChange = { viewModel.sendIntent(SignInIntent.ChangeEmail(it)) },
                placeholder = { Text("Enter Email") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                singleLine = true,
                onValueChange = { viewModel.sendIntent(SignInIntent.ChangePassword(it)) },
                placeholder = { Text("Enter Password") },
                visualTransformation =
                    if (state.passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.sendIntent(SignInIntent.SwitchPasswordVisibility) }) {
                        Icon(
                            imageVector = if (state.passwordVisible)
                                ImageVector.vectorResource(id = R.drawable.lock_24px)
                            else ImageVector.vectorResource(id = R.drawable.lock_open_right_24px),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null,
                        )
                    }
                }
            )
            TextButton(
                onClick = { viewModel.sendIntent(SignInIntent.SignIn) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Sign In", style = TextStyle(
                        fontSize = 16.sp
                    )
                )
            }
        }
    }

}


