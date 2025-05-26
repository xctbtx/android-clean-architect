package com.xctbtx.cleanarchitectsample.domain.user.usecase

import com.xctbtx.cleanarchitectsample.domain.user.model.User
import com.xctbtx.cleanarchitectsample.domain.user.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: String): User {
        return repository.getUserById(id)
    }
}