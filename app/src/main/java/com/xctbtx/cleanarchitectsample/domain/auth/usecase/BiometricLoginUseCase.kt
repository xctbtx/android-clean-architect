package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import android.content.Context
import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.SaveBiometricInfoUseCase.Companion.TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.Charset
import java.security.InvalidKeyException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class BiometricLoginUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val authRepo: AuthenticationRepository
) {
    private val executor = ContextCompat.getMainExecutor(context)

    operator fun invoke(
        activity: FragmentActivity,
        onResult: (String?) -> Unit,
        onError: (String) -> Unit
    ) {
        val encryptedData = authRepo.loadEncryptedData()
        if (encryptedData == null) {
            onResult(null)
            return
        }
        val promptInfo = createPromptInfo()
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    try {
                        val cipher = result.cryptoObject?.cipher
                        if (cipher != null) {
                            val decrypted = cipher.doFinal(encryptedData.cipherText)
                            val userId = decrypted.toString(Charset.defaultCharset())
                            onResult(userId)
                        }else{
                            onError("Unknown error")
                        }
                    } catch (e: Exception) {
                        onError(e.message ?: "Unknown error")
                    }
                }

                override fun onAuthenticationError(code: Int, errString: CharSequence) {
                    onError(errString.toString())
                }
            }
        )
        try {
            val cipher = authRepo.getCipher()
            val key = authRepo.getSecretKey()
            cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(encryptedData.iv))
            biometricPrompt.authenticate(
                promptInfo,
                BiometricPrompt.CryptoObject(cipher)
            )
        } catch (_: InvalidKeyException) {
            onError("Key is invalid")
            Log.e(TAG, "Key is invalid.")
            //authRepo.generateKey()
            //TODO: generate new key to use
            this.invoke(activity, onResult, onError)
        } catch (_: UserNotAuthenticatedException) {
            Log.d(TAG, "The key's validity timed out.")
            onError("The key's validity timed out.")
            biometricPrompt.authenticate(promptInfo)
        }

    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login using fingerprint")
            .setSubtitle("Please touch the sensor")
            .setNegativeButtonText("Cancel")
            .build()
    }
}
