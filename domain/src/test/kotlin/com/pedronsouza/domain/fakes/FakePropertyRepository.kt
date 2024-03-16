package com.pedronsouza.domain.fakes

import com.pedronsouza.domain.PropertyPriceCalculator
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.repositories.PropertyRepository
import com.pedronsouza.domain.values.AppCurrency

object FakePropertyRepository : PropertyRepository {
    override suspend fun fetch(): List<Property> =
        listOf(FakeProperty)

    fun fetchResultWithCurrencyRateApplied(
        selectedCurrency: AppCurrency,
        currencies: List<Currency>
    ) =
        PropertyPriceCalculator.prepareValueWithCurrencyRateApplied(
            FakeProperty,
            currencies,
            selectedCurrency
        )
}