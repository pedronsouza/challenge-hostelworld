package com.pedronsouza.domain.values

@JvmInline
value class SelectedCurrency(val currencyCode: String) {
    override fun toString() = currencyCode
}
