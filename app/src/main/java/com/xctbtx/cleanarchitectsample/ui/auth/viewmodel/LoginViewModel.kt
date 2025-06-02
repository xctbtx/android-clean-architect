package com.xctbtx.cleanarchitectsample.ui.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.LoginUseCase
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.RegisterUseCase
import com.xctbtx.cleanarchitectsample.ui.auth.state.LoginUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    var username: String = ""
    var password: String = ""

    fun onUsernameChange(username: String) {
        this.username = username
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun performLogin() {
        viewModelScope.launch {
            loginUseCase(username, password, object : ApiCallBack {
                override fun onSuccess() {
                    //
                }

                override fun onFailure(error: String) {
                    //
                }

            })
        }
    }

    fun performRegister() {
        viewModelScope.launch {
            registerUseCase(username, password, object : ApiCallBack {
                override fun onSuccess() {
                    //
                }

                override fun onFailure(error: String) {
                    //
                }

            })
        }
    }

}
