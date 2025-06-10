package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepo: AuthenticationRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): UserDto? {
        return authRepo.performLogin(username, password)
    }
}
