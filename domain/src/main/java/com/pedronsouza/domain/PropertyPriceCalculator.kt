package com.pedronsouza.domain

import com.pedronsouza.domain.exceptions.CurrencyNotAvailableException
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.values.AppCurrency

internal object PropertyPriceCalculator {
    fun prepareValueWithCurrencyRateApplied(
        property: Property,
        currencies: List<Currency>,
        selectedCurrency: AppCurrency
    ): Property {
        val rate = try {
            currencies.first { it.currencyCode == selectedCurrency.toString() }.rate
        } catch (e: Throwable) {
            throw CurrencyNotAvailableException()
        }

        return property.copy(
            lowestPriceByNightWithRateApplied = (property.lowestPriceByNight * rate)
        )
    }
}