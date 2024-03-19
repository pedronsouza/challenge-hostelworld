package com.pedronsouza.shared.navigation

import com.pedronsouza.shared.AppScreen

interface RouteFactory {
    fun createRoute(screen: AppScreen, parameter: List<String>?): String
}

class RouteFactoryImpl : RouteFactory {
    override fun createRoute(screen: AppScreen, parameter: List<String>?): String =
        when (screen) {
            AppScreen.HOME -> AppScreen.HOME.toString()
            AppScreen.DETAIL -> {
                screen.toString().replace(
                    "{${screen.parameters?.get(0)}}/{${screen.parameters?.get(1)}}",
                    "${parameter?.get(0)}/${parameter?.get(1)}"
                )
            }
        }
}