package com.xctbtx.cleanarchitectsample.ui.phone.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.xctbtx.cleanarchitectsample.ui.main.viewmodel.MainViewModel
import com.xctbtx.cleanarchitectsample.ui.phone.viewmodel.DialerViewModel

@Composable
fun DialerScreen(mVM: MainViewModel) {

    val viewModel: DialerViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.phoneNumber,
                onValueChange = viewModel::updatePhoneNumber
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PhoneButton("1")
            PhoneButton("2")
            PhoneButton("3")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            PhoneButton("4")
            PhoneButton("5")
            PhoneButton("6")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            PhoneButton("7")
            PhoneButton("8")
            PhoneButton("9")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            PhoneButton("*")
            PhoneButton("0")
            PhoneButton("#")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            PhoneButton("Call", onClick = {
                mVM.onCallClick(uiState.phoneNumber)
            })
        }
    }
}

@Composable
fun PhoneButton(text: String, onClick: (() -> Unit)? = null) {
    val viewModel: DialerViewModel = hiltViewModel()
    Button(onClick = {
        if (onClick == null) {
            viewModel.appendNumber(text)
        } else {
            onClick.invoke()
        }
    }) {
        Text(text)
    }
}