package com.xctbtx.cleanarchitectsample.data.auth

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val api: FireStoreApiService
) : AuthenticationRepository {
    override suspend fun performLogin(username: String, password: String): UserDto? {
        return api.login(username, password)
    }

    override suspend fun performRegister(
        payload: UserDto,
        password: String,
        callBack: ApiCallBack
    ) {
        api.signIn(payload, password, callBack)
    }
}