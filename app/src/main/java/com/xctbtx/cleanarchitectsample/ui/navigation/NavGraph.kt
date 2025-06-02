package com.xctbtx.cleanarchitectsample.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.ConversationScreen
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.NewConversationScreen
import com.xctbtx.cleanarchitectsample.ui.auth.screen.LoginScreen
import com.xctbtx.cleanarchitectsample.ui.menu.screen.MenuItem
import com.xctbtx.cleanarchitectsample.ui.menu.screen.MenuScreen
import com.xctbtx.cleanarchitectsample.ui.message.screen.MessageScreen
import com.xctbtx.cleanarchitectsample.ui.navigation.NavHelper.navigateSingleTopTo
import com.xctbtx.cleanarchitectsample.ui.navigation.NavHelper.navigateToSingleConversation
import com.xctbtx.cleanarchitectsample.ui.post.screen.PostScreen
import com.xctbtx.cleanarchitectsample.ui.user.screen.UsersScreen

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
        composable(route = Conversation.route) {
            ConversationScreen(
                navigateToMessage = { conversationId ->
                    navController.navigateToSingleConversation(conversationId)
                },
                navigateToNewConversation = {
                    navController.navigate(NewConversation.route)
                }
            )
        }
        composable(route = Menu.route) {
            MenuScreen()
        }
        composable(route = NewConversation.route) {
            NewConversationScreen()
        }
        composable(route = User.route) {
            UsersScreen()
        }
        composable(route = Post.route) {
            PostScreen()
        }
        composable(
            route = Message.routeWithArgs,
            arguments = Message.arguments
        ) { navBackStackEntry ->
            val conversationId = navBackStackEntry.arguments?.getString(Message.conversationIdArg)
            MessageScreen(conversationId)
        }

        composable(
            route = Login.route
        ) {
            LoginScreen { userId ->
                navController.navigateSingleTopTo(Post.route)
            }
        }
    }
}

interface Destination {
    val route: String
}

interface MenuItem : Destination {
    val title: String
    val icon: ImageVector
    val onClick: () -> Unit
}


interface BottomDestination : Destination {
    val icon: ImageVector
    val label: String
    val contentDescription: String
}

object Conversation : BottomDestination {
    override val route = "conversation"
    override val label = "Chats"
    override val contentDescription = "Chats"
    override val icon = Icons.Outlined.Email
}

object Post : BottomDestination {
    override val route = "post"
    override val label = "Posts"
    override val contentDescription = "Posts"
    override val icon = Icons.Default.Person
}

object Menu : BottomDestination {
    override val route = "menu"
    override val label = "Menu"
    override val contentDescription = "Menu"
    override val icon = Icons.Default.Menu
}

object Message : Destination {
    override val route = "message"
    const val conversationIdArg = "conversation_id"
    val routeWithArgs = "${route}/{${conversationIdArg}}"
    val arguments = listOf(
        navArgument(conversationIdArg) { type = NavType.StringType }
    )
}

object NewConversation : Destination {
    override val route = "new_conversation"
}

object User : Destination {
    override val route = "user"
}

object Login : Destination {
    override val route = "login"
}
