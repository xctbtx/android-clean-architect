package com.xctbtx.cleanarchitectsample.ui.auth.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.ui.auth.viewmodel.AuthViewModel
import com.xctbtx.cleanarchitectsample.ui.main.viewmodel.MainViewModel

const val TAG = "LoginScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(mVM: MainViewModel, onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    val viewModel: AuthViewModel = hiltViewModel()
    val state = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.error)
                }
            }

            else -> {
                LoginContainer(
                    viewModel,
                    mVM,
                    padding,
                    onLoginSuccess,
                    onRegisterClick
                )
                SimpleDialog(viewModel, mVM, onLoginSuccess)
            }
        }
    }
}

@Composable
fun SimpleDialog(viewModel: AuthViewModel, mVM: MainViewModel, onLoginSuccess: () -> Unit) {
    Column {
        if (viewModel.showDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.hideDialog() },
                title = { Text("Biometric supported") },
                text = { Text("Do you want to enable login by Biometric ?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.hideDialog()
                        mVM.saveUserId(viewModel.uiState.user.id, {
                            viewModel.showToast("Save user id success.")
                            onLoginSuccess()
                        }, { e ->
                            viewModel.showToast("Failed: ${e.cause}")
                        })
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.hideDialog() }) {
                        Text("Skip")
                    }
                }
            )
        }
    }
}

@Composable
fun LoginContainer(
    viewModel: AuthViewModel,
    mVM: MainViewModel,
    paddingValues: PaddingValues,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.command.collect { command ->
            when (command) {
                is AuthViewModel.AuthUiCommand.ShowToast -> Toast.makeText(
                    context,
                    command.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = viewModel.uiState.user.username,
            label = { Text("User name") },
            onValueChange = viewModel::onUsernameChange,
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            trailingIcon = {
                Icon(Icons.Default.Lock, null, modifier = Modifier.clickable {
                    mVM.loginWihBiometric({ userId ->
                        if (userId == null) {
                            viewModel.onUserIdNull()
                        }
                    }, { _ -> })
                })
            }
        )
        OutlinedTextField(
            value = viewModel.uiState.user.password,
            label = { Text("Password") },
            onValueChange = viewModel::onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontStyle = FontStyle.Normal)) {
                    append("Don't have account ?")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Register")
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
                .clickable {
                    onRegisterClick()
                })
        Button(
            onClick = {
                viewModel.performLogin(object : ApiCallBack {
                    override fun onSuccess() {
                        //Ask if save userId or not ?
                        mVM.checkBiometric { isAvailable ->
                            if (!isAvailable) {
                                onLoginSuccess()
                            } else {
                                viewModel.onBiometricAvailable()
                            }
                        }
                    }

                    override fun onFailure(error: String) {
                        Log.d(TAG, "performLogin onFailure: $error")
                    }

                })
            }, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 50.dp)
        ) {
            Text("Login")
        }
    }

}