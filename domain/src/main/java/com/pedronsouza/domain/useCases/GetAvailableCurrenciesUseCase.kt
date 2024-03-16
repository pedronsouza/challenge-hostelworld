package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.values.AppCurrency

interface GetAvailableCurrenciesUseCase {
    suspend fun execute(): Result<List<AppCurrency>>
}

internal class GetAvailableCurrenciesUseCaseImpl : GetAvailableCurrenciesUseCase {
    override suspend fun execute(): Result<List<AppCurrency>> =
        Result.success(
            listOf(
                AppCurrency("EUR"),
                AppCurrency("USD"),
                AppCurrency("GBP")
            )
        )
}