package com.pedronsouza.shared

enum class AppScreen(val parameterName: String? = null) {
    HOME,
    DETAIL("propertyId");

    override fun toString(): String =
        when(this) {
            HOME -> "/home"
            DETAIL -> "/property_detail/{$parameterName}"
        }
}