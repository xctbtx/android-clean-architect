package com.xctbtx.cleanarchitectsample.domain.user.repository

import com.xctbtx.cleanarchitectsample.domain.user.model.User

interface UserRepository {
    suspend fun getUserById(id: String): User
}