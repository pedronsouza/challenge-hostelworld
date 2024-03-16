package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency

interface SaveSelectedCurrencyUseCase {
    fun execute(currency: AppCurrency): Result<Unit>
}

internal class SaveSelectedCurrencyUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : SaveSelectedCurrencyUseCase {
    override fun execute(currency: AppCurrency): Result<Unit> =
        runCatching {
            currencyRepository.setSelectedCurrency(currency)
        }
}