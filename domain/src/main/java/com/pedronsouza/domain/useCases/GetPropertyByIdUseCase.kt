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

interface GetPropertyByIdUseCase {
    suspend fun execute(propertyId: String): Result<Property>
}

internal class GetPropertyByIdUseCaseImpl(
    private val propertyRepository: PropertyRepository,
    private val currencyRepository: CurrencyRepository,
    private val instanceScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

) : GetPropertyByIdUseCase {
    override suspend fun execute(propertyId: String): Result<Property> =
        runCatching {
            val deferredResults = listOf(
                instanceScope.async {propertyRepository.getById(propertyId) },
                instanceScope.async { currencyRepository.getCurrencies() },
                instanceScope.async { currencyRepository.getSelectedCurrency() }
            )

            val results = deferredResults.awaitAll()

            val property = results[0] as Property
            val currencies = results[1] as List<Currency>
            val selectedCurrency = results[2] as AppCurrency

            PropertyPriceCalculator.prepareValueWithCurrencyRateApplied(
                property = property,
                currencies = currencies,
                selectedCurrency = selectedCurrency
            )
        }
}