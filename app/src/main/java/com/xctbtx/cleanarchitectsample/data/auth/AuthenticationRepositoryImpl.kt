package com.xctbtx.cleanarchitectsample.data.auth

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import com.xctbtx.cleanarchitectsample.domain.auth.repository.EncryptedData
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val api: FireStoreApiService
) : AuthenticationRepository {
    override suspend fun performLogin(username: String, password: String): UserDto? {
        return api.login(username, password)
    }

    override suspend fun performRegister(
        payload: UserDto,
        password: String,
        callBack: ApiCallBack
    ) {
        api.signIn(payload, password, callBack)
    }

    private val keyAlias = "biometric_key_alias"

    @RequiresApi(Build.VERSION_CODES.R)
    override fun encrypt(data: ByteArray): EncryptedData {
        val cipher = getCipher(Cipher.ENCRYPT_MODE)
        val encrypted = cipher.doFinal(data)
        return EncryptedData(encrypted, cipher.iv)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        val cipher = getCipher(Cipher.DECRYPT_MODE, iv)
        return cipher.doFinal(data)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getCipher(mode: Int, iv: ByteArray? = null): Cipher {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val key = keyStore.getKey(keyAlias, null) as? SecretKey ?: generateKey()
        val cipher = Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        )
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(mode, key)
        } else {
            cipher.init(mode, key, IvParameterSpec(iv))
        }
        return cipher
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun generateKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val paramSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setUserAuthenticationParameters(0, KeyProperties.AUTH_BIOMETRIC_STRONG)
            .build()

        keyGen.init(paramSpec)
        return keyGen.generateKey()
    }

}