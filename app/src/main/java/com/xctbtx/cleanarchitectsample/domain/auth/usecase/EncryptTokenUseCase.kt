package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import com.xctbtx.cleanarchitectsample.domain.auth.repository.EncryptedData
import javax.inject.Inject

class EncryptTokenUseCase @Inject constructor(private val repo: AuthenticationRepository) {
    suspend operator fun invoke(token: String): EncryptedData {
        return repo.encrypt(token.toByteArray())
    }
}