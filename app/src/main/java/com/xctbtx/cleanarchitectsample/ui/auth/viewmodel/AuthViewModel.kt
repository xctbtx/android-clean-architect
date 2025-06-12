package com.xctbtx.cleanarchitectsample.ui.auth.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.user.mapper.UserMapper.toDto
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.LoginUseCase
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.RegisterUseCase
import com.xctbtx.cleanarchitectsample.ui.auth.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(AuthUiState())
        private set

    fun onUsernameChange(username: String) {
        uiState = uiState.copy(user = uiState.user.copy(username = username))
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(user = uiState.user.copy(password = password))
    }

    fun onConfirmPasswordChange(confirmPass: String) {
        val isValid = confirmPass == uiState.user.password
        uiState = uiState.copy(isValidate = isValid)
    }

    fun onFullNameChange(name: String) {
        uiState = uiState.copy(user = uiState.user.copy(name = name))
    }

    fun onAddressChange(address: String) {
        uiState = uiState.copy(user = uiState.user.copy(address = address))
    }

    fun onDobChange(dob: String) {
        Log.d("TAG", "onDobChange: ")
        uiState = uiState.copy(user = uiState.user.copy(dob = dob))
    }

    fun onAvatarChange(uri: Uri?) {
        uiState = uiState.copy(user = uiState.user.copy(avatar = uri))
    }

    fun performLogin(callBack: ApiCallBack) {
        viewModelScope.launch {
            val result = loginUseCase(uiState.user.username, uiState.user.password)
            if (result != null) {
                callBack.onSuccess()
            } else {
                callBack.onFailure("Username or password is incorrect")
            }
        }
    }

    fun performRegister(callBack: ApiCallBack) {
        viewModelScope.launch {
            registerUseCase(uiState.user.toDto(), uiState.user.password, callBack)
        }
    }

}
