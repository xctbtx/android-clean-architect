package com.xctbtx.cleanarchitectsample.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.ConversationScreen
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.NewConversationScreen
import com.xctbtx.cleanarchitectsample.ui.main.screen.HomeScreen
import com.xctbtx.cleanarchitectsample.ui.message.screen.MessageScreen
import com.xctbtx.cleanarchitectsample.ui.post.screen.PostScreen

object Routes {
    const val HOME = "home"
    const val POST = "posts"
    const val CONVERSATION = "conversation"
    const val NEW_CONVERSATION = "new_conversation"
    const val MESSAGE = "message"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.HOME -> HomeScreen()
                    Destination.CONVERSATION -> ConversationScreen(navController)
                    Destination.NEW_CONVERSATION -> NewConversationScreen(navController)
                    Destination.MESSAGE -> MessageScreen(navController)
                    Destination.POSTS -> PostScreen()
                }
            }
        }
    }
}

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String,
    val isBottomEntry: Boolean = true
) {
    CONVERSATION(Routes.CONVERSATION, "Chat", Icons.Default.Email, "Conversation"),
    HOME(Routes.HOME, "Home", Icons.Default.Home, "Home"),
    MESSAGE(Routes.MESSAGE, "Message", Icons.Default.MailOutline, "Message", isBottomEntry = false),
    NEW_CONVERSATION(
        Routes.NEW_CONVERSATION,
        "Conversation",
        Icons.Default.Create,
        "New Conversation",
        isBottomEntry = false
    ),
    POSTS(Routes.POST, "Posts", Icons.Default.Person, "Posts")
}
