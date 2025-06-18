package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import android.content.Context
import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import com.xctbtx.cleanarchitectsample.domain.auth.repository.EncryptedData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.Charset
import java.security.InvalidKeyException
import javax.crypto.Cipher
import javax.inject.Inject

class SaveBiometricInfoUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val authRepo: AuthenticationRepository
) {
    private val executor = ContextCompat.getMainExecutor(context)

    operator fun invoke(
        activity: FragmentActivity,
        userId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val promptInfo = createPromptInfo()
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    val cipher = result.cryptoObject?.cipher
                    if (cipher != null) {
                        val encryptedInfo: ByteArray = cipher.doFinal(
                            userId.toByteArray(Charset.defaultCharset())
                        )
                        val encryptedData = EncryptedData(encryptedInfo, cipher.iv)
                        authRepo.saveEncryptedData(encryptedData)
                        onSuccess.invoke()
                    } else {
                        Log.d(TAG, "onAuthenticationSucceeded: but cipher = null")
                        onError("Result cipher = null error")
                    }
                }

                override fun onAuthenticationError(code: Int, errString: CharSequence) {
                    onError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    onError("Something wrong.")
                }
            }
        )
        try {
            val cipher = authRepo.getCipher()
            val key = authRepo.getSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, key)
            biometricPrompt.authenticate(
                promptInfo,
                BiometricPrompt.CryptoObject(cipher)
            )
        } catch (_: InvalidKeyException) {
            onError("Key is invalid")
            Log.e(TAG, "Key is invalid.")
            authRepo.getSecretKey()
            this.invoke(activity, userId, onSuccess, onError)
        } catch (_: UserNotAuthenticatedException) {
            Log.d(TAG, "The key's validity timed out.")
            onError("The key's validity timed out.")
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirm to save login information")
            .setSubtitle("Please touch the sensor")
            .setNegativeButtonText("Cancel")
            .build()
    }

    companion object {
        const val TAG = "SaveBiometricInfoUseCase"
    }
}