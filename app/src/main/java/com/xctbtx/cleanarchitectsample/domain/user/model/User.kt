package com.xctbtx.cleanarchitectsample.domain.user.model

import com.google.firebase.firestore.Exclude

data class User(
    @get:Exclude
    val id: String = "",
    val name: String = "",
    val avatar: String = "",
    val dob: Int = 0
)