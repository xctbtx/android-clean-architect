package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(username: String, password: String, apiCallBack: ApiCallBack) {

    }
}
