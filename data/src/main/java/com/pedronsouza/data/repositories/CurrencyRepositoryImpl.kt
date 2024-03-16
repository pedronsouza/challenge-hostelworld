package com.pedronsouza.data.repositories

import com.pedronsouza.data.api.CurrencyApi
import com.pedronsouza.data.internal.ServicesFactory
import com.pedronsouza.data.mappers.GetCurrencyResponseMapper
import com.pedronsouza.domain.dataSources.LocalCurrencyDataSource
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency

internal class CurrencyRepositoryImpl(
    private val servicesFactory: ServicesFactory,
    private val cache: LocalCurrencyDataSource,
    private val mapper: GetCurrencyResponseMapper
) : CurrencyRepository {
    private val remoteApi: CurrencyApi by lazy { servicesFactory.getOrCreate(CurrencyApi::class) }

    override suspend fun getCurrencies(): List<Currency> {
        if (!cache.isWarmed())  {
            remoteApi.getCurrencies().apply {
                cache.addToCache(
                    list = mapper.transform(this)
                )
            }
        }

        return cache.getCurrencies()
    }

    override fun getSelectedCurrency(): AppCurrency =
        cache.getSelectedCurrency()

    override fun setSelectedCurrency(newCurrency: AppCurrency) {
        cache.setSelectedCurrency(newCurrency)
    }
}