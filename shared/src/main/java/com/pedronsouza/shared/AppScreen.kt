package com.pedronsouza.shared

enum class AppScreen(val parameters: List<String>? = null) {
    HOME,
    DETAIL(listOf("propertyId", "propertyName"));

    override fun toString(): String =
        when(this) {
            HOME -> "/home"
            DETAIL -> "/property_detail/{${parameters?.first()}}/{${parameters?.last()}}"
        }
}