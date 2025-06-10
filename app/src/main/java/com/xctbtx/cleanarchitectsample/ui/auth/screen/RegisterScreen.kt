package com.xctbtx.cleanarchitectsample.ui.auth.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xctbtx.cleanarchitectsample.ui.auth.viewmodel.AuthViewModel

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
@Preview
fun preview() {
    RegisterContainer(PaddingValues(1.dp), {})
}

@Composable
fun RegisterContainer(
    paddingValues: PaddingValues,
    onRegisterSuccess: () -> Unit
) {
    //val viewModel: LoginViewModel = hiltViewModel()
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = "viewModel.name",
            label = { Text("Full name") },
            onValueChange = {},//viewModel::onFullNameChange,
            colors = TextFieldDefaults.colors().copy(
                errorContainerColor = Color.Red,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        TextField(
            value = "viewModel.address",
            label = { Text("Address") },
            onValueChange = {},//viewModel::onAddressChange,
            colors = TextFieldDefaults.colors().copy(
                errorContainerColor = Color.Red,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        TextField(
            value = "viewModel.avatar",
            label = { Text("Avatar") },
            onValueChange = {},//viewModel::onAvatarChange,
            colors = TextFieldDefaults.colors().copy(
                errorContainerColor = Color.Red,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        Button(
            onClick = {}//{
//                viewModel.performLogin(object : ApiCallBack {
//                    override fun onSuccess() {
//                        onLoginSuccess("")
//                    }
//
//                    override fun onFailure(error: String) {
//                        Log.d(TAG, "performLogin onFailure: $error")
//                    }
//
//                })
            //       }
            , modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 50.dp)
        ) {
            Text("Register")
        }
    }

}

//@Composable
//fun DateSingleTextField() {
//    var dateText by remember { mutableStateOf("") }
//
//    OutlinedTextField(
//        value = dateText,
//        onValueChange = { input ->
//            // Chỉ cho nhập số và dấu /
//            val filtered = input.filter { it.isDigit() || it == '/' }
//
//            // Tự động thêm dấu / khi đủ ký tự
//            val digits = filtered.filter { it.isDigit() }
//            val formatted = buildString {
//                for (i in digits.indices) {
//                    append(digits[i])
//                    if (i == 1 || i == 3) append('/')
//                }
//            }
//
//            // Giới hạn độ dài (dd/MM/yyyy => 10 ký tự)
//            dateText = formatted.take(10)
//        },
//        label = Text("Date (dd/MM/yyyy)"),
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//        modifier = Modifier.padding(16.dp)
//    )
//
//    Spacer(modifier = Modifier.height(8.dp))
//    Text(text = "You entered: $dateText")
//}