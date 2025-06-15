package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
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
        onError: (Throwable) -> Unit
    ) {
        val cipher = authRepo.getCipher(Cipher.ENCRYPT_MODE)
        val prompt = createPromptInfo()

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    try {
                        val data = userId.toByteArray()
                        val encrypted = authRepo.encrypt(data)
                        authRepo.saveEncryptedData(encrypted)
                        onSuccess()
                    } catch (e: Exception) {
                        Log.e("TAG", "onAuthenticationSucceeded: $e")
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

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Xác thực để lưu User ID")
            .setSubtitle("Xác thực bằng vân tay")
            .setNegativeButtonText("Huỷ")
            .build()
    }
}