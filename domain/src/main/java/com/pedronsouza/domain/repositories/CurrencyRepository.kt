package com.pedronsouza.domain.repositories

import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.values.AppCurrency

interface CurrencyRepository {
    suspend fun getCurrencies(): List<Currency>
    fun getSelectedCurrency(): AppCurrency
    fun setSelectedCurrency(newCurrency: AppCurrency)
}