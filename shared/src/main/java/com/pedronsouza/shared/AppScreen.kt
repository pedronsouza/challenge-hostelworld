package com.pedronsouza.shared

enum class AppScreen {
    HOME,
    DETAIL;

    override fun toString(): String =
        when(this) {
            HOME -> "/home"
            DETAIL -> "/property_detail/{propertyId}"
        }
}