package com.pedronsouza.domain

import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.values.AppCurrency

internal object PropertyPriceCalculator {
    fun prepareValueWithCurrencyRateApplied(
        property: Property,
        currencies: List<Currency>,
        selectedCurrency: AppCurrency
    ): Property {
        val rate = currencies.first { it.currencyCode == selectedCurrency.toString() }.rate

        return property.copy(
            lowestPriceByNightWithRateApplied = (property.lowestPriceByNight * rate)
        )
    }
}