package com.pedronsouza.shared.navigation

import com.pedronsouza.shared.AppScreen
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

interface RouteFactory {
    fun createRoute(screen: AppScreen, parameter: String?): String
}

class RouteFactoryImpl : RouteFactory {
    @OptIn(ExperimentalEncodingApi::class)
    override fun createRoute(screen: AppScreen, parameter: String?): String =
        when (screen) {
            AppScreen.HOME -> AppScreen.HOME.toString()
            AppScreen.DETAIL -> {
                checkNotNull(parameter)

                screen.toString().replace(
                    "{${screen.parameterName}}",
                    Base64.encode(parameter.toByteArray())
                )
            }
        }
}