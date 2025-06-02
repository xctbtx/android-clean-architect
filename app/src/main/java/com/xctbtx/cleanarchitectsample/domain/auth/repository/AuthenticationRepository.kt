package com.xctbtx.cleanarchitectsample.domain.auth.repository

interface AuthenticationRepository {
    suspend fun performLogin(username: String, password: String)
    suspend fun performRegister(username: String, password: String)
}