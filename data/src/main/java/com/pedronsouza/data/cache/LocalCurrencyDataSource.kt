package com.pedronsouza.data.cache

import com.pedronsouza.data.internal.Constants
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.SelectedCurrency

internal interface LocalCurrencyDataSource : CurrencyRepository {
    fun isWarmed(): Boolean
    fun addToCache(list: List<Currency>)
    fun setSelectedCurrency(newCurrency: SelectedCurrency)
}

internal class LocalCurrencyDataSourceImpl : LocalCurrencyDataSource {
    private val currencyCache = mutableListOf<Currency>()
    private var selectedCurrency: SelectedCurrency = SelectedCurrency(Constants.DefaultCurrencyCode)

    override fun isWarmed() = currencyCache.isNotEmpty()

    override fun addToCache(list: List<Currency>) =
        currencyCache.plusAssign(list)

    override suspend fun getCurrencies() = currencyCache

    override fun getSelectedCurrency() = selectedCurrency

    override fun setSelectedCurrency(newCurrency: SelectedCurrency) {
        selectedCurrency = newCurrency
    }
}