package com.xctbtx.cleanarchitectsample.domain.user.usecase

import com.xctbtx.cleanarchitectsample.domain.user.repository.UserRepository
import javax.inject.Inject

class GetUserAvatarUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: String): String {
        return repository.getUserById(id).avatar
    }
}