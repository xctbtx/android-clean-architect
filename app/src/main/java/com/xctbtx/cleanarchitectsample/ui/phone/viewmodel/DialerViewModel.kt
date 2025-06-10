package com.xctbtx.cleanarchitectsample.ui.phone.viewmodel

import androidx.lifecycle.ViewModel
import com.xctbtx.cleanarchitectsample.domain.message.usecase.GetMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DialerViewModel @Inject constructor(
    private val getMessageUseCase: GetMessageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CallUiState())
    val uiState: StateFlow<CallUiState> = _uiState.asStateFlow()

    fun updatePhoneNumber(number: String) {
        _uiState.update { it.copy(phoneNumber = number) }
    }

    fun appendNumber(text: String) {
        _uiState.update { it.copy(phoneNumber = it.phoneNumber + text) }
    }

    data class CallUiState(
        val phoneNumber: String = ""
    )
}
