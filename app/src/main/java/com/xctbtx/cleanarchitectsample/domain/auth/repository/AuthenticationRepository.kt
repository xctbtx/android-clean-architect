package com.xctbtx.cleanarchitectsample.domain.auth.repository

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto

interface AuthenticationRepository {
    suspend fun performLogin(username: String, password: String): UserDto?
    suspend fun performRegister(payload: UserDto, password: String, callBack: ApiCallBack)
}