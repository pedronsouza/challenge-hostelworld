package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.PropertyPriceCalculator
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.repositories.PropertyRepository
import com.pedronsouza.domain.values.AppCurrency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

interface LoadPropertiesUseCase {
    suspend fun execute(): Result<List<Property>>
}

internal class LoadPropertiesUseCaseImpl(
    private val propertyRepository: PropertyRepository,
    private val currencyRepository: CurrencyRepository,
) : LoadPropertiesUseCase {
    private val instanceScope = CoroutineScope(Dispatchers.IO)

    override suspend fun execute() =
        runCatching {
            val deferredResults = listOf(
                instanceScope.async { propertyRepository.fetch() },
                instanceScope.async { currencyRepository.getCurrencies() },
                instanceScope.async { currencyRepository.getSelectedCurrency() }
            )

            val results = deferredResults.awaitAll()

            val properties = results[0] as List<Property>
            val currencies = results[1] as List<Currency>
            val selectedCurrency = results[2] as AppCurrency

            properties.map { property ->
                PropertyPriceCalculator.prepareValueWithCurrencyRateApplied(
                    property = property,
                    currencies = currencies,
                    selectedCurrency = selectedCurrency
                )
            }
        }
}