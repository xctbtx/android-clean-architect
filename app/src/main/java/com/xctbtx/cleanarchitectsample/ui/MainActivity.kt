package com.xctbtx.cleanarchitectsample.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telecom.TelecomManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.BiometricLoginUseCase
import com.xctbtx.cleanarchitectsample.domain.auth.usecase.SaveBiometricInfoUseCase
import com.xctbtx.cleanarchitectsample.ui.main.screen.MainScaffold
import com.xctbtx.cleanarchitectsample.ui.main.viewmodel.MainViewModel
import com.xctbtx.cleanarchitectsample.ui.service.FireStoreService
import com.xctbtx.cleanarchitectsample.ui.theme.CleanArchitectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity(), OnRequestPermissionsResultCallback {

    @Inject
    lateinit var firestore: FireStoreApiService

    @Inject
    lateinit var biometricLoginUseCase: BiometricLoginUseCase

    @Inject
    lateinit var saveBiometricInfoUseCase: SaveBiometricInfoUseCase

    private lateinit var launcher: ActivityResultLauncher<Any?>

    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeEvents()
        setContent {
            CleanArchitectTheme {
                MainScaffold(viewModel)
            }
        }
        launcher = registerForActivityResult(contracts, rsCallBack)
    }

    private val contracts = object : ActivityResultContract<Any?, Boolean>() {
        @RequiresApi(Build.VERSION_CODES.R)
        override fun createIntent(context: Context, input: Any?): Intent {
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
            }
            return enrollIntent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return checkBiometric()
        }

    }

    private val rsCallBack = ActivityResultCallback<Boolean> {
        Log.d(TAG, "ActivityResultCallback: $it")
    }

    private fun checkBiometric(): Boolean {
        var result = false
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(TAG, "App can authenticate using biometrics.")
                result = true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e(TAG, "No biometric features available on this device.")
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e(TAG, "Biometric features are currently unavailable.")
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                launcher.launch("")
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.e(TAG, "Error: security update is required.")
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.e(TAG, "Unsupported android version.")
            }

            else -> {
                Log.e(TAG, "Unknown biometric status.")
            }
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun observeEvents() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is MainViewModel.UiEvent.RequestCall -> {
                            Log.d("TAG", "RequestCall: ${event.phoneNumber}")
                            performCall(event.phoneNumber)
                        }

                        is MainViewModel.UiEvent.SaveUserId -> {
                            saveUserIdWithBiometric(event.userId, event.onSuccess, event.onError)
                        }

                        is MainViewModel.UiEvent.LoginWithBiometric -> {
                            loginWithBiometric(event.onResult, event.onError)
                        }

                        is MainViewModel.UiEvent.CheckBiometric -> {
                            val isAvailable = checkBiometric()
                            event.onResult(isAvailable)
                        }

                        else -> Log.d("TAG", "Un-handle case")
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun loginWithBiometric(
        onResult: (String?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        biometricLoginUseCase(this, onResult, onError)
    }

    private fun saveUserIdWithBiometric(
        userId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        saveBiometricInfoUseCase(this, userId, onSuccess, onError)
    }

    private fun performCall(number: String) {
        val uri = "tel:$number".toUri()
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CODE
            )
            return
        }
        telecomManager.placeCall(uri, null)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        Log.d("TAG", "onRequestPermissionsResult: $grantResults")
        if (requestCode == REQUEST_CODE) {
            val grantedResults = permissions.zip(grantResults.toTypedArray()).toMap()
                .filterValues { it == PackageManager.PERMISSION_GRANTED }
            grantedResults.keys.forEach {
                when (it) {
                    Manifest.permission.CALL_PHONE -> {
                        performCall(viewModel.phoneNumber)
                    }

                    Manifest.permission.POST_NOTIFICATIONS -> {
                        startService(Intent(this, FireStoreService::class.java))
                    }

                    else -> {
                        //un-handle
                    }
                }

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
    }

    override fun onDestroy() {
        firestore.detachAllListener()
        super.onDestroy()
    }

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_CODE = 10101
    }
}