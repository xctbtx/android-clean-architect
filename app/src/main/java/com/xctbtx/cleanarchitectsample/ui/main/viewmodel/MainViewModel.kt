package com.xctbtx.cleanarchitectsample.ui.main.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var phoneNumber: String = ""
    var curDesIndex by mutableIntStateOf(1)

    fun onCallClick(number: String) {
        phoneNumber = number
        viewModelScope.launch {
            _event.emit(UiEvent.RequestCall(number))
        }
    }

    fun saveUserId(
        userId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _event.emit(UiEvent.SaveUserId(userId, onSuccess, onError))
        }
    }

    fun loginWihBiometric(
        onResult: (String?) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _event.emit(UiEvent.LoginWithBiometric(onResult, onError))
        }
    }

    fun checkBiometric(
        onResult: (Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            _event.emit(UiEvent.CheckBiometric(onResult))
        }
    }

    fun onDestinationChange(index: Int) {
        curDesIndex = index
    }

    sealed interface UiEvent {
        data class RequestCall(val phoneNumber: String) : UiEvent
        data class SaveUserId(
            val userId: String,
            val onSuccess: () -> Unit,
            val onError: (String) -> Unit
        ) : UiEvent

        data class LoginWithBiometric(
            val onResult: (String?) -> Unit,
            val onError: (String) -> Unit
        ) : UiEvent

        data class EndCall(val value: String) : UiEvent
        data class CheckBiometric(val onResult: (Boolean) -> Unit) : UiEvent
    }
}
