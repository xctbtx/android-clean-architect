package com.xctbtx.cleanarchitectsample.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.ConversationScreen
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.NewConversationScreen
import com.xctbtx.cleanarchitectsample.ui.menu.screen.MenuScreen
import com.xctbtx.cleanarchitectsample.ui.message.screen.MessageScreen
import com.xctbtx.cleanarchitectsample.ui.post.screen.PostScreen
import com.xctbtx.cleanarchitectsample.ui.user.screen.UsersScreen

object Routes {
    const val POST = "posts"
    const val CONVERSATION = "conversation"
    const val NEW_CONVERSATION = "new_conversation"
    const val MESSAGE = "message"
    const val MENU = "message"
    const val USER = "message"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.CONVERSATION -> ConversationScreen(navController)
                    Destination.NEW_CONVERSATION -> NewConversationScreen()
                    Destination.MESSAGE -> MessageScreen()
                    Destination.POSTS -> PostScreen()
                    Destination.MENU -> MenuScreen()
                    Destination.USER -> UsersScreen()
                }
            }
        }
    }
}

enum class Destination(
    val route: String,
    val label: String = "",
    val contentDescription: String = "",
    val isBottomEntry: Boolean = true,
    val icon: ImageVector = Icons.Default.Home
) {
    CONVERSATION(Routes.CONVERSATION, "Chat", "Conversation", icon = Icons.Outlined.Email),
    POSTS(Routes.POST, "Posts", "Posts", icon = Icons.Default.Person),
    MENU(Routes.MENU, "Menu", "Menu", icon = Icons.Default.Menu),
    MESSAGE(Routes.MESSAGE, isBottomEntry = false),
    NEW_CONVERSATION(Routes.NEW_CONVERSATION, isBottomEntry = false),
    USER(Routes.USER, isBottomEntry = false)
}
