package com.pedronsouza.domain.values

import java.util.Currency

@JvmInline
value class AppCurrency(internal val currencyCode: String) {
    override fun toString() = currencyCode
}

val AppCurrency.displayName: String get() = Currency.getInstance(currencyCode).displayName
