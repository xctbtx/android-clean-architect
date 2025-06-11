package com.xctbtx.cleanarchitectsample.ui.auth.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

data class UserUiModel(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val address: String = "",
    val avatar: Any? = Icons.Default.AccountCircle,
    val dob: String? = null
)