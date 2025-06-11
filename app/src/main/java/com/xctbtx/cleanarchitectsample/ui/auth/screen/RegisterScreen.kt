package com.xctbtx.cleanarchitectsample.ui.auth.screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.ui.auth.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    val viewModel: AuthViewModel = hiltViewModel()
    val state = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.error)
                }
            }

            else -> {
                RegisterContainer(padding, onRegisterSuccess)
            }
        }
    }
}

@Composable
fun DatePickerFieldToModal(viewModel: AuthViewModel, modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf(viewModel.uiState.user.dob) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate ?: "",
        onValueChange = { viewModel.onDobChange(it) },
        label = { Text("DOB") },
        shape = RoundedCornerShape(16.dp),
        placeholder = { Text("DD/MM/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = convertMillisToDate(it ?: 0) },
            onDismiss = { showModal = false }
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun RegisterContainer(
    paddingValues: PaddingValues,
    onRegisterSuccess: () -> Unit
) {
    val viewModel: AuthViewModel = hiltViewModel()
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarPicker(viewModel)
        FormatedTextField(
            value = viewModel.uiState.user.name,
            label = "Full name",
            onValueChange = viewModel::onFullNameChange,
        )
        FormatedTextField(
            value = viewModel.uiState.user.address,
            label = "Address",
            onValueChange = viewModel::onAddressChange,
        )
        DatePickerFieldToModal(viewModel)
        Button(
            onClick = {
                viewModel.performRegister(object : ApiCallBack {
                    override fun onSuccess() {
                        onRegisterSuccess()
                    }

                    override fun onFailure(error: String) {
                        Log.d(TAG, "onFailure: $error")
                    }

                })
            }, modifier = Modifier
                .padding(top = 50.dp)
        ) {
            Text("Register")
        }
    }

}

@Composable
fun AvatarPicker(viewModel: AuthViewModel) {
    val image = viewModel.uiState.user.avatar
    FlexibleImage(
        viewModel,
        imageVector = if (image is ImageVector) image else null,
        imageUri = if (image is Uri) image else null
    )
}

@Composable
fun FlexibleImage(
    viewModel: AuthViewModel,
    imageVector: ImageVector? = null,
    imageUri: Uri? = null
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onAvatarChange(uri)
    }
    val painter = when {
        imageVector != null -> rememberVectorPainter(imageVector)
        imageUri != null -> rememberAsyncImagePainter(model = imageUri)
        else -> null
    }

    if (painter != null) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                },
            contentScale = ContentScale.Crop
        )
    } else {
        // Optional: show a placeholder or nothing
        Box(modifier = Modifier.background(Color.Gray))
    }
}

@Composable
fun FormatedTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        label = { Text(label) },
        onValueChange = onValueChange,
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        keyboardOptions = keyboardOptions
    )
}

