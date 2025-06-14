package com.xctbtx.cleanarchitectsample.domain.auth.usecase

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.crypto.Cipher
import javax.inject.Inject

class BiometricLoginUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val authRepo: AuthenticationRepository
) {
    private val executor = ContextCompat.getMainExecutor(context)

    @RequiresApi(Build.VERSION_CODES.R)
    operator fun invoke(
        activity: FragmentActivity,
        onResult: (String?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val encryptedData = authRepo.loadEncryptedData()
        if (encryptedData == null) {
            onResult(null)
            return
        }

        val cipher = authRepo.getCipher(Cipher.DECRYPT_MODE, encryptedData.iv)
        val prompt = createPromptInfo("Login using fingerprint")

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    try {
                        val decrypted = authRepo.decrypt(encryptedData.cipherText, encryptedData.iv)
                        val userId = decrypted.toString()
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

    private fun createPromptInfo(title: String): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle("Xác thực bằng vân tay")
            .setNegativeButtonText("Huỷ")
            .build()
    }
}
