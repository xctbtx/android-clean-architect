package com.xctbtx.cleanarchitectsample.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    // One-time event stream
    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onCallClick(number: String) {
        viewModelScope.launch {
            _event.emit(UiEvent.RequestCall(number))
        }
    }

    fun saveUserId(
        userId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            _event.emit(UiEvent.SaveUserId(userId, onSuccess, onError))
        }
    }

    fun loginWihBiometric(
        onResult: (String?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Log.d("TAG", "loginWihBiometric clicked ")
        viewModelScope.launch {
            _event.emit(UiEvent.LoginWithBiometric(onResult, onError))
        }
    }


    sealed interface UiEvent {
        data class RequestCall(val phoneNumber: String) : UiEvent
        data class SaveUserId(
            val userId: String,
            val onSuccess: () -> Unit,
            val onError: (Throwable) -> Unit
        ) : UiEvent

        data class LoginWithBiometric(
            val onResult: (String?) -> Unit,
            val onError: (Throwable) -> Unit
        ) : UiEvent

        data class EndCall(val value: String) : UiEvent
    }
}
