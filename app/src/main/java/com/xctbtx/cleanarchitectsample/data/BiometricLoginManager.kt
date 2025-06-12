package com.xctbtx.cleanarchitectsample.data

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricLoginManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AuthenticationRepository,
    private val secureStorage: SecureStorage
) {

    private val executor = ContextCompat.getMainExecutor(context)
    private val keyAlias = "biometric_key_alias"

    fun saveUserId(
        activity: FragmentActivity,
        userId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val cipher = getEncryptCipher()
        val prompt = createPromptInfo("Xác thực để lưu User ID")

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    try {
                        val encrypted = repository.encrypt(userId.toByteArray())
                        secureStorage.saveEncryptedData(encrypted)
                        onSuccess()
                    } catch (e: Exception) {
                        onError(e)
                    }
                }

                override fun onAuthenticationError(code: Int, errString: CharSequence) {
                    onError(Exception(errString.toString()))
                }
            }
        )

        biometricPrompt.authenticate(prompt, BiometricPrompt.CryptoObject(cipher))
    }

    fun tryLogin(
        activity: FragmentActivity,
        onResult: (String?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val encryptedData = secureStorage.loadEncryptedData()
        if (encryptedData == null) {
            onResult(null)
            return
        }

        val cipher = getDecryptCipher(encryptedData.iv)
        val prompt = createPromptInfo("Login using fingerprint")

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    try {
                        val decrypted =
                            repository.decrypt(encryptedData.cipherText, encryptedData.iv)
                        val userId = decrypted.toString(Charsets.UTF_8)
                        onResult(userId)
                    } catch (e: Exception) {
                        onError(e)
                    }
                }

                override fun onAuthenticationError(code: Int, errString: CharSequence) {
                    onError(Exception(errString.toString()))
                }
            }
        )

        biometricPrompt.authenticate(prompt, BiometricPrompt.CryptoObject(cipher))
    }

    private fun getEncryptCipher(): Cipher {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
        return Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.ENCRYPT_MODE, secretKey)
        }
    }

    private fun getDecryptCipher(iv: ByteArray): Cipher {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
        return Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        }
    }

    private fun createPromptInfo(title: String): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle("Xác thực bằng vân tay")
            .setNegativeButtonText("Huỷ")
            .build()
    }
}