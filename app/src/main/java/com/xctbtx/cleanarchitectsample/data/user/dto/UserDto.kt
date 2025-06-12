package com.xctbtx.cleanarchitectsample.data.user.dto

import com.google.firebase.firestore.Exclude

data class UserDto(
    @get:Exclude
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val address: String = "",
    val avatar: String = "",
    val dob: String? = null
)
