package com.pedronsouza.data.repositories

import com.pedronsouza.data.api.CurrencyApi
import com.pedronsouza.data.cache.LocalCurrencyDataSourceImpl
import com.pedronsouza.data.internal.ServicesFactory
import com.pedronsouza.data.mappers.GetCurrencyResponseMapper
import com.pedronsouza.data.responses.GetCurrenciesResponse
import com.pedronsouza.domain.dataSources.LocalCurrencyDataSource
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.values.AppCurrency
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRepositoryImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val currencyApi = object: CurrencyApi {
        override suspend fun getCurrencies() =
            GetCurrenciesResponse(
                "EUR",
                mapOf("EUR" to 1.0)
            )
    }

    private val servicesFactory = object: ServicesFactory {
        override fun <T> getOrCreate(type: KClass<*>): T = currencyApi as T
    }


    @Test
    fun `Given cache is not warmed up When requesting currencies Then should acquire then from the server, save on cache and return the expected list of currencies`() {
        val expectedCurrencies = listOf(Currency("EUR", 1.0))

        val localCurrencyDataSource = object: LocalCurrencyDataSource {
            private var newCurrencies = listOf<Currency>()
            override fun addToCache(list: List<Currency>) { newCurrencies = list }
            override fun isWarmed() = false
            override suspend fun getCurrencies(): List<Currency> = newCurrencies
            override fun getSelectedCurrency(): AppCurrency = AppCurrency("EUR")
            override fun setSelectedCurrency(newCurrency: AppCurrency, clearCache: Boolean) = Unit
        }

        val mapper = object: GetCurrencyResponseMapper {
            override fun transform(inputData: GetCurrenciesResponse): List<Currency> =
                expectedCurrencies
        }

        val subject = CurrencyRepositoryImpl(
            servicesFactory,
            localCurrencyDataSource,
            mapper
        )

        runTest(testDispatcher) {
            val result = subject.getCurrencies()
            assertEquals(expectedCurrencies, result)
        }
    }

    @Test
    fun `Given cache is warmed up When requesting for currencies Then should retrieve currencies from cache`() {
        val expectedCacheList = listOf(Currency("TSR", 1.0))
        val localCurrencyDataSource = object: LocalCurrencyDataSource {
            override fun addToCache(list: List<Currency>) = Unit
            override fun isWarmed() = true
            override suspend fun getCurrencies(): List<Currency> = expectedCacheList
            override fun getSelectedCurrency(): AppCurrency = AppCurrency("TSR")
            override fun setSelectedCurrency(newCurrency: AppCurrency, clearCache: Boolean) = Unit
        }

        val mapper = object: GetCurrencyResponseMapper {
            override fun transform(inputData: GetCurrenciesResponse): List<Currency> =
                expectedCacheList
        }

        val subject = CurrencyRepositoryImpl(
            servicesFactory,
            localCurrencyDataSource,
            mapper
        )

        runTest(testDispatcher) {
            val result = subject.getCurrencies()
            assertEquals(expectedCacheList, result)
        }
    }


    @Test
    fun `Given currencyApi is failing AND cache is not warmed up When requesting for currencies Then it should throws the remoteApi exception`() {
        val expectedException = UnsupportedOperationException()

        val localCurrencyDataSource = object: LocalCurrencyDataSource {
            private var newCurrencies = listOf<Currency>()
            override fun addToCache(list: List<Currency>) { newCurrencies = list }
            override fun isWarmed() = false
            override suspend fun getCurrencies(): List<Currency> = newCurrencies
            override fun getSelectedCurrency(): AppCurrency = AppCurrency("EUR")
            override fun setSelectedCurrency(newCurrency: AppCurrency, clearCache: Boolean) = Unit
        }

        val mapper = object: GetCurrencyResponseMapper {
            override fun transform(inputData: GetCurrenciesResponse): List<Currency> =
                emptyList()
        }

        val subject = CurrencyRepositoryImpl(
            object:ServicesFactory {
                override fun <T> getOrCreate(type: KClass<*>): T = object: CurrencyApi {
                    override suspend fun getCurrencies(): GetCurrenciesResponse {
                        throw expectedException
                    }
                } as T

            },
            localCurrencyDataSource,
            mapper
        )

        runTest(testDispatcher) {
            assertFailsWith<UnsupportedOperationException> {
                subject.getCurrencies()
            }
        }
    }

    @Test
    fun `Given a valid instance When requesting for the selected currency Then should return what is saved in memory`() {
        val localCurrencyDataSource = LocalCurrencyDataSourceImpl().apply {
            setSelectedCurrency(AppCurrency("EUR"), false)
        }

        val mapper = object: GetCurrencyResponseMapper {
            override fun transform(inputData: GetCurrenciesResponse): List<Currency> =
                listOf(Currency("EUR",1.0))
        }

        val subject = CurrencyRepositoryImpl(
            servicesFactory,
            localCurrencyDataSource,
            mapper
        )

        assertEquals(AppCurrency("EUR"), subject.getSelectedCurrency())
    }

    @Test
    fun `Given a valid instance When set a new currency Then should replace what is saved in memory`() {
        val localCurrencyDataSource = LocalCurrencyDataSourceImpl().apply {
            setSelectedCurrency(AppCurrency("EUR"), false)
        }

        val mapper = object: GetCurrencyResponseMapper {
            override fun transform(inputData: GetCurrenciesResponse): List<Currency> =
                listOf(Currency("EUR",1.0), Currency("TST",1.0))
        }

        val subject = CurrencyRepositoryImpl(
            servicesFactory,
            localCurrencyDataSource,
            mapper
        )

        subject.setSelectedCurrency(AppCurrency("TST"), false)

        assertEquals(AppCurrency("TST"), subject.getSelectedCurrency())
    }
}