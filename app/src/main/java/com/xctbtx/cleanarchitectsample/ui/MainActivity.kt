package com.xctbtx.cleanarchitectsample.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telecom.TelecomManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.ui.main.screen.MainScaffold
import com.xctbtx.cleanarchitectsample.ui.main.viewmodel.MainViewModel
import com.xctbtx.cleanarchitectsample.ui.theme.CleanArchitectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), OnRequestPermissionsResultCallback {

    @Inject
    lateinit var firestore: FireStoreApiService

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeEvents()
        setContent {
            CleanArchitectTheme {
                MainScaffold(viewModel)
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is MainViewModel.UiEvent.RequestCall -> {
                            Log.d("TAG", "RequestCall: ${event.phoneNumber}")
                            performCall(event.phoneNumber)
                        }

                        else -> Log.d("TAG", "Un-handle case")
                    }
                }
            }
        }
    }

    private fun performCall(number: String) {
        val uri = "tel:$number".toUri()
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#v
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 112)
//             here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
//             to handle the case where the user grants the permission. See the documentation
//             for ActivityCompat#requestPermissions for more details.
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
        Log.d(
            "TAG",
            "onRequestPermissionsResult: ${grantResults[0] == PackageManager.PERMISSION_GRANTED}"
        )
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 11) {
            performCall(requestCode.toString())
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
    }

    override fun onDestroy() {
        firestore.detachAllListener()
        super.onDestroy()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}