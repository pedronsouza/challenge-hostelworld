package com.pedronsouza.shared_test.fakes

import com.pedronsouza.domain.dataSources.LocalCurrencyDataSource
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.values.AppCurrency

val FakeLocalCurrencyDataSource = object: LocalCurrencyDataSource {
    private var newCurrencies = listOf<Currency>()
    override fun addToCache(list: List<Currency>) { newCurrencies = list }
    override fun isWarmed() = false
    override suspend fun getCurrencies(): List<Currency> = newCurrencies
    override fun getSelectedCurrency(): AppCurrency = AppCurrency("EUR")
    override fun setSelectedCurrency(newCurrency: AppCurrency, clearCache: Boolean) = Unit
}