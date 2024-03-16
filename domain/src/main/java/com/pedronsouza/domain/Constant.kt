package com.pedronsouza.domain

import com.pedronsouza.domain.values.AppCurrency

internal object Constant {
    val availableCurrencies = listOf(
        AppCurrency("EUR"),
        AppCurrency("USD"),
        AppCurrency("GBP")
    )
}