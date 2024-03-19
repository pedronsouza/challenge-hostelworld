package com.pedronsouza.domain.fakes

import com.pedronsouza.domain.Constant
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency

object FakeCurrencyRepository : CurrencyRepository {
    private var selectedCurrency = AppCurrency("EUR")

    override suspend fun getCurrencies(): List<Currency> =
        listOf(
            Currency("EUR", 1.0),
            Currency("USD", 1.3),
            Currency("GBL", 0.83),
        )

    override fun getSelectedCurrency(): AppCurrency =
        selectedCurrency

    override fun setSelectedCurrency(newCurrency: AppCurrency, clearCache: Boolean) {
        selectedCurrency = newCurrency
    }

    override fun getAvailableCurrencies(): List<AppCurrency> = Constant.availableCurrencies
}