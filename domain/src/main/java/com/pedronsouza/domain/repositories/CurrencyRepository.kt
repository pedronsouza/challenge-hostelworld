package com.pedronsouza.domain.repositories

import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.values.SelectedCurrency

interface CurrencyRepository {
    suspend fun getCurrencies(): List<Currency>
    fun getSelectedCurrency(): SelectedCurrency
}