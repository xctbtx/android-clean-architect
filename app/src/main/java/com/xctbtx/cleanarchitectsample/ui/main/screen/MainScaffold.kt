package com.xctbtx.cleanarchitectsample.ui.main.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.xctbtx.cleanarchitectsample.ui.main.viewmodel.MainViewModel
import com.xctbtx.cleanarchitectsample.ui.navigation.AppNavGraph
import com.xctbtx.cleanarchitectsample.ui.navigation.Conversation
import com.xctbtx.cleanarchitectsample.ui.navigation.Login
import com.xctbtx.cleanarchitectsample.ui.navigation.Menu
import com.xctbtx.cleanarchitectsample.ui.navigation.Post

@Composable
fun MainScaffold(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val destinations = listOf(Post, Conversation, Menu)
    val startDestination = Login
    val currentDes = viewModel.curDesIndex
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                destinations.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = currentDes == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            viewModel.onDestinationChange(index)
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { contentPadding ->
        AppNavGraph(
            viewModel,
            navController,
            startDestination,
            modifier = Modifier.padding(contentPadding)
        )
    }
}
