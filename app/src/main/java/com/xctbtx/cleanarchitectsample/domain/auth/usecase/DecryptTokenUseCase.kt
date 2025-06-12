package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import javax.inject.Inject

class DecryptTokenUseCase @Inject constructor(private val repo: AuthenticationRepository) {
    suspend operator fun invoke(cipherText: ByteArray, iv: ByteArray): String {
        val plain = repo.decrypt(cipherText, iv)
        return String(plain)
    }
}