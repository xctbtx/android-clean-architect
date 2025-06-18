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
import com.xctbtx.cleanarchitectsample.data.user.mapper.UserMapper.toUiModel
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.LoginUseCase
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.RegisterUseCase
import com.xctbtx.cleanarchitectsample.ui.auth.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(AuthUiState())
        private set

    private val _command = MutableSharedFlow<AuthUiCommand>()
    val command = _command.asSharedFlow()

    var showDialog by mutableStateOf(false)
        private set

    fun onUserIdNull() {
        viewModelScope.launch {
            _command.emit(AuthUiCommand.ShowToast("Please login with password before using biometric."))
        }
    }

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
        val username = uiState.user.username
        val password = uiState.user.password
        if (username.trim().isEmpty() || password.trim()
                .isEmpty()
        ) callBack.onFailure("Invalid user/password")
        viewModelScope.launch {
            val result = loginUseCase(username, password)
            if (result != null) {
                callBack.onSuccess()
                uiState = uiState.copy(user = result.toUiModel())
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

    fun showToast(message: String) {
        viewModelScope.launch {
            _command.emit(AuthUiCommand.ShowToast(message))
        }
    }

    fun onBiometricAvailable() {
        showDialog = true
    }

    fun hideDialog() {
        showDialog = false
    }

    sealed interface AuthUiCommand {
        data class ShowToast(val message: String) : AuthUiCommand
    }
}
