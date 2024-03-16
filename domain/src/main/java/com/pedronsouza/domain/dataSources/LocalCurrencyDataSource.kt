package com.pedronsouza.domain.dataSources

import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency

interface LocalCurrencyDataSource : CurrencyRepository {
    fun isWarmed(): Boolean
    fun addToCache(list: List<Currency>)
}