package com.xctbtx.cleanarchitectsample.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xctbtx.cleanarchitectsample.data.Cache
import com.xctbtx.cleanarchitectsample.ui.auth.screen.LoginScreen
import com.xctbtx.cleanarchitectsample.ui.auth.screen.RegisterScreen
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.ConversationScreen
import com.xctbtx.cleanarchitectsample.ui.conversation.screen.NewConversationScreen
import com.xctbtx.cleanarchitectsample.ui.main.viewmodel.MainViewModel
import com.xctbtx.cleanarchitectsample.ui.menu.screen.MenuScreen
import com.xctbtx.cleanarchitectsample.ui.message.screen.MessageScreen
import com.xctbtx.cleanarchitectsample.ui.navigation.NavHelper.navigateSingleTopTo
import com.xctbtx.cleanarchitectsample.ui.navigation.NavHelper.navigateToSingleConversation
import com.xctbtx.cleanarchitectsample.ui.phone.screen.DialerScreen
import com.xctbtx.cleanarchitectsample.ui.post.screen.PostScreen
import com.xctbtx.cleanarchitectsample.ui.profile.screen.ProfileScreen

@Composable
fun AppNavGraph(
    viewModel: MainViewModel,
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
            MenuScreen(
                toDialerScreen = {
                    navController.navigate(Dialer.route)
                },
                toProfileScreen = {
                    navController.navigateSingleTopTo(Profile.route)
                })
        }
        composable(route = NewConversation.route) {
            NewConversationScreen()
        }
        composable(route = Profile.route) {
            ProfileScreen { navController.navigateSingleTopTo(Login.route) }
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
            LoginScreen(viewModel, onLoginSuccess = {
                navController.navigateSingleTopTo(Conversation.route)
                Cache.currentUserId = it
            }, onRegisterClick = {
                navController.navigateSingleTopTo(Register.route)
            })
        }

        composable(
            route = Register.route
        ) {
            RegisterScreen(viewModel) {
                navController.navigateSingleTopTo(Login.route)
            }
        }

        composable(
            route = Dialer.route
        ) {
            DialerScreen(viewModel)
        }
    }
}

interface Destination {
    val route: String
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
    override val icon = Icons.Default.Email
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

object Dialer : Destination {
    override val route = "dialer"
}

object NewConversation : Destination {
    override val route = "new_conversation"
}

object Profile : Destination {
    override val route = "profile"
}

object Login : Destination {
    override val route = "login"
}

object Register : Destination {
    override val route = "register"
}
