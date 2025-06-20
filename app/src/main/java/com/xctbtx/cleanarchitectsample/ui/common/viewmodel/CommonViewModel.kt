package com.xctbtx.cleanarchitectsample.ui.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

open class CommonViewModel<T , V>(initialState: T) : ViewModel() {

    var uiState by mutableStateOf(initialState)
        protected set

    protected val _command = MutableSharedFlow<V>()
    val command = _command.asSharedFlow()
}