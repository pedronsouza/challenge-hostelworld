package com.pedronsouza.shared.extensions

import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.values.AppCurrency
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

internal fun Property.priceFormatted(selectedCurrency: AppCurrency): String =
    try {
        NumberFormat.getCurrencyInstance(Locale.getDefault()).run {
            currency = Currency.getInstance(selectedCurrency.toString())
            format(lowestPriceByNightWithRateApplied)
        }
    } catch (e: ArithmeticException) {
        lowestPriceByNightWithRateApplied.toString()
    }