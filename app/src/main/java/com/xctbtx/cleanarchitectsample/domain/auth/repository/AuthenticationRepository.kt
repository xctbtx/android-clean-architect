package com.xctbtx.cleanarchitectsample.domain.auth.repository

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import javax.crypto.Cipher

interface AuthenticationRepository {
    suspend fun performLogin(username: String, password: String): UserDto?
    suspend fun performRegister(payload: UserDto, password: String, callBack: ApiCallBack)
    fun encrypt(data: ByteArray): EncryptedData
    fun decrypt(data: ByteArray, iv: ByteArray): ByteArray
    fun loadEncryptedData(): EncryptedData?
    fun saveEncryptedData(data: EncryptedData)
    fun getCipher(mode: Int, iv: ByteArray? = null): Cipher
}

data class EncryptedData(
    val cipherText: ByteArray,
    val iv: ByteArray
)