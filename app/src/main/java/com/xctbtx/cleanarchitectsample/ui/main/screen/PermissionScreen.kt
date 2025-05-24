package com.xctbtx.cleanarchitectsample.ui.main.screen

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Preview
fun PermissionScreen() {
    val permission = android.Manifest.permission.POST_NOTIFICATIONS
    val notificationPermissionState = rememberPermissionState(permission)
    var showRationale by remember { mutableStateOf(false) }
    val needRequest = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    LaunchedEffect(Unit) {
        if (needRequest && !notificationPermissionState.status.isGranted) {
            if (notificationPermissionState.status.shouldShowRationale) {
                showRationale = true
            } else {
                notificationPermissionState.launchPermissionRequest()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            if (notificationPermissionState.status.isGranted) "Notification permission granted."
            else "Notification permission not granted."
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (needRequest && !notificationPermissionState.status.isGranted) {
            Button(onClick = {
                if (notificationPermissionState.status.shouldShowRationale) {
                    showRationale = true
                } else {
                    notificationPermissionState.launchPermissionRequest()
                }
            }) {
                Text("Should request notification permission")
            }
        }
    }

    // Custom rationale UI
    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text("Allow notification") },
            text = { Text("This app need notification permission to show notification about incoming message.") },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    notificationPermissionState.launchPermissionRequest()
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationale = false }) {
                    Text("No thanks")
                }
            }
        )
    }
}