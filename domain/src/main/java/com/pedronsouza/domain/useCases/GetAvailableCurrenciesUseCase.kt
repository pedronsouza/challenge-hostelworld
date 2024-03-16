package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency

interface GetAvailableCurrenciesUseCase {
    suspend fun execute(): Result<List<AppCurrency>>
}

internal class GetAvailableCurrenciesUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : GetAvailableCurrenciesUseCase {
    override suspend fun execute(): Result<List<AppCurrency>> =
        runCatching {
            currencyRepository.getAvailableCurrencies()
        }
}