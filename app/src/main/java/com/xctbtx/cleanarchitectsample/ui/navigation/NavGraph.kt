package com.xctbtx.cleanarchitectsample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.ConversationScreen
import com.xctbtx.cleanarchitectsample.ui.main.screen.MainScreen
import com.xctbtx.cleanarchitectsample.ui.message.screen.MessageScreen
import com.xctbtx.cleanarchitectsample.ui.post.screen.PostScreen

object Routes {
    const val MAIN = "main"
    const val POST = "posts"
    const val CONVERSATION = "conversation"
    const val MESSAGE = "message"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
        composable(Routes.MAIN) {
            MainScreen(navController)
        }
        composable(Routes.POST) {
            PostScreen()
        }
        composable(Routes.CONVERSATION) {
            ConversationScreen(navController)
        }
        composable(Routes.MESSAGE) {
            MessageScreen(navController)
        }
    }
}
