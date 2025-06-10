package com.xctbtx.cleanarchitectsample.ui.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.LoginUseCase
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.RegisterUseCase
import com.xctbtx.cleanarchitectsample.ui.auth.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun onUsernameChange(username: String) {
        this.username = username
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun performLogin(callBack: ApiCallBack) {
        viewModelScope.launch {
            val result = loginUseCase(username, password)
            if (result != null) {
                callBack.onSuccess()
            } else {
                callBack.onFailure("Username or password is incorrect")
            }
        }
    }

    fun performRegister() {
        viewModelScope.launch {
            val user = UserDto()
            registerUseCase(user, password, object : ApiCallBack {
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
