package com.xctbtx.cleanarchitectsample.data.auth

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.AUTH_BIOMETRIC_STRONG
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.security.keystore.KeyProperties.BLOCK_MODE_CBC
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.Constants
import com.xctbtx.cleanarchitectsample.data.SecureStorage
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import com.xctbtx.cleanarchitectsample.domain.auth.repository.EncryptedData
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val api: FireStoreApiService,
    private val secureStorage: SecureStorage,
) : AuthenticationRepository {

    private val keyAlias = "biometric_key_alias"

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


    override fun loadEncryptedData(): EncryptedData? {
        return secureStorage.loadEncryptedData()
    }

    override fun saveEncryptedData(data: EncryptedData) {
        secureStorage.saveEncryptedData(data)
    }


    override fun getCipher(): Cipher {
        val cipher =
            Cipher.getInstance("${KEY_ALGORITHM_AES}/${BLOCK_MODE_CBC}/${ENCRYPTION_PADDING_PKCS7}")
        return cipher
    }

    override fun generateKey(): SecretKey {
        val keyGen =
            KeyGenerator.getInstance(KEY_ALGORITHM_AES, Constants.ANDROID_KEY_STORE)
        val pBuilder = KeyGenParameterSpec.Builder(
            keyAlias,
            PURPOSE_ENCRYPT or PURPOSE_DECRYPT
        )
            .setBlockModes(BLOCK_MODE_CBC)
            .setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
        if (Build.VERSION.SDK_INT >= 30) {
            pBuilder.setUserAuthenticationParameters(
                0,
                AUTH_BIOMETRIC_STRONG
            )
        } else {
            @Suppress("DEPRECATION")
            pBuilder.setUserAuthenticationValidityDurationSeconds(0)
        }
        keyGen.init(pBuilder.build())
        return keyGen.generateKey()
    }

    override fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        return keyStore.getKey(keyAlias, null) as? SecretKey ?: generateKey()
    }

    companion object {
        const val TAG = "AuthenticationRepositoryImpl"
    }
}