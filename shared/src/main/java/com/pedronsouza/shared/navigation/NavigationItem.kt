package com.pedronsouza.shared.navigation

import com.pedronsouza.shared.AppScreen

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(AppScreen.HOME.toString())
}