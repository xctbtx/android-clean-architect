package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.xctbtx.cleanarchitectsample.data.SecureStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class BiometricLoginUseCase @Inject constructor(@ApplicationContext val context: Context) {

    @RequiresApi(Build.VERSION_CODES.P)
    suspend operator fun invoke() {
        val secureStorage = SecureStorage(context)
        val encryptedData = secureStorage.loadEncryptedData()
        if (encryptedData != null) {
            val cipher = getDecryptCipher("biometric_key_alias", encryptedData.iv)
            //biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun getDecryptCipher(alias: String, iv: ByteArray): Cipher {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = keyStore.getKey(alias, null) as SecretKey
        return Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        }
    }
}