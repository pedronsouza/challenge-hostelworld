package com.pedronsouza.shared.navigation

import com.pedronsouza.shared.AppScreen

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(AppScreen.HOME.toString())
    data object Detail : NavigationItem(AppScreen.DETAIL.toString())
}