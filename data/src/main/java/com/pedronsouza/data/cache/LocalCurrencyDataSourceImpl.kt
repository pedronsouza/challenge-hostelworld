package com.pedronsouza.data.cache

import com.pedronsouza.data.internal.Constants
import com.pedronsouza.domain.dataSources.LocalCurrencyDataSource
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.values.AppCurrency

internal class LocalCurrencyDataSourceImpl : LocalCurrencyDataSource {
    private val currencyCache = mutableListOf<Currency>()
    private var selectedCurrency: AppCurrency = AppCurrency(Constants.DefaultCurrencyCode)

    override fun isWarmed() = currencyCache.isNotEmpty()

    override fun addToCache(list: List<Currency>) =
        currencyCache.plusAssign(list)

    override suspend fun getCurrencies() = currencyCache

    override fun getSelectedCurrency() = selectedCurrency

    override fun setSelectedCurrency(newCurrency: AppCurrency) {
        selectedCurrency = newCurrency
    }
}