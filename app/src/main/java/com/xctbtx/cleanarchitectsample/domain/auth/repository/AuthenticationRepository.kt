package com.xctbtx.cleanarchitectsample.domain.auth.repository

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import javax.crypto.Cipher
import javax.crypto.SecretKey

interface AuthenticationRepository {
    suspend fun performLogin(username: String, password: String): UserDto?
    suspend fun performRegister(payload: UserDto, password: String, callBack: ApiCallBack)
    fun loadEncryptedData(): EncryptedData?
    fun saveEncryptedData(data: EncryptedData)
    fun getCipher(): Cipher
    fun getSecretKey(): SecretKey
    fun generateSecretKey()
}

data class EncryptedData(
    val cipherText: ByteArray,
    val iv: ByteArray
)