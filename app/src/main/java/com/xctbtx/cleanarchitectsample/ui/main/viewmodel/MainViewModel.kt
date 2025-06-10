package com.xctbtx.cleanarchitectsample.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    // One-time event stream
    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onCallClick(number: String) {
        Log.d("TAG", "onCallClick: ")
        viewModelScope.launch {
            _event.emit(UiEvent.RequestCall(number))
        }
    }


    sealed interface UiEvent {
        data class RequestCall(val phoneNumber: String) : UiEvent
        data class EndCall(val value: String) : UiEvent
    }
}
