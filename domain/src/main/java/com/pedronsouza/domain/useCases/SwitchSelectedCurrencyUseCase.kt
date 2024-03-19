package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency

interface SwitchSelectedCurrencyUseCase {
    fun execute(currency: AppCurrency): Result<Unit>
}

internal class SwitchSelectedCurrencyUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : SwitchSelectedCurrencyUseCase {
    override fun execute(currency: AppCurrency): Result<Unit> =
        runCatching {
            currencyRepository.setSelectedCurrency(currency, true)
        }
}