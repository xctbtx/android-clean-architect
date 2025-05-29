package com.xctbtx.cleanarchitectsample.ui.navigation

import androidx.navigation.NavHostController

object NavHelper {

    fun NavHostController.navigateSingleTopTo(route: String) =
        this.navigate(route) { launchSingleTop = true }

    fun NavHostController.navigateToSingleConversation(conversationId: String) =
        this.navigateSingleTopTo("${Message.route}/$conversationId")
}