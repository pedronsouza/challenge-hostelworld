package com.pedronsouza.shared.navigation

import com.pedronsouza.shared.AppScreen
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals

class RouteFactoryImplTest {

    @Test
    fun `Given a app screen type that doesnt have a parameter When creating the route Then should return the expected uri segment`() {
        val subject = RouteFactoryImpl()
        val expectedUrl = AppScreen.HOME.toString()

        assertEquals(expectedUrl, subject.createRoute(AppScreen.HOME, null))
    }

    @Test
    fun `Given a app screen type that have a parameter When creating the route Then should return the expected uri segment with the parameter encoded`() {
        val subject = RouteFactoryImpl()
        val parameter = listOf("paramId", "paramName")
        val expectedUrl = AppScreen.DETAIL.toString()
            .replace("{${AppScreen.DETAIL.parameters?.get(0).orEmpty()}}/{${AppScreen.DETAIL.parameters?.get(1).orEmpty()}}", "paramId/paramName")

        assertEquals(expectedUrl, subject.createRoute(AppScreen.DETAIL, parameter))
    }
}