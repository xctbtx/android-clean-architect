package com.xctbtx.cleanarchitectsample.data

import android.content.Context
import android.util.Base64
import com.xctbtx.cleanarchitectsample.domain.auth.repository.EncryptedData
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorage @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    fun saveEncryptedData(data: EncryptedData) {
        prefs.edit {
            putString("cipher", Base64.encodeToString(data.cipherText, Base64.DEFAULT))
            putString("iv", Base64.encodeToString(data.iv, Base64.DEFAULT))
        }
    }

    fun loadEncryptedData(): EncryptedData? {
        val cipherText = prefs.getString("cipher", null)
        val iv = prefs.getString("iv", null)
        return if (cipherText != null && iv != null) {
            EncryptedData(
                Base64.decode(cipherText, Base64.DEFAULT),
                Base64.decode(iv, Base64.DEFAULT)
            )
        } else null
    }
}