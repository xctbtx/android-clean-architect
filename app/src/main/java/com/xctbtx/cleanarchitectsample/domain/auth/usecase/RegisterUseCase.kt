package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepo: AuthenticationRepository
) {
    suspend operator fun invoke(user: UserDto, password: String, apiCallBack: ApiCallBack) {
        authRepo.performRegister(user, password, apiCallBack)
    }
}
