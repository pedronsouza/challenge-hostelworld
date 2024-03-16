package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency

interface GetSelectedCurrencyUseCase {
    suspend fun execute(): Result<AppCurrency>
}

internal class GetSelectedCurrencyUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : GetSelectedCurrencyUseCase {
    override suspend fun execute(): Result<AppCurrency> =
        runCatching {
            currencyRepository.getSelectedCurrency()
        }
}