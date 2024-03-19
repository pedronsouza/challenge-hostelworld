package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.fakes.FakeCurrencyRepository
import com.pedronsouza.domain.fakes.FakePropertyRepository
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.repositories.PropertyRepository
import com.pedronsouza.domain.values.AppCurrency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class LoadPropertiesUseCaseImplTest {
    @Test
    fun `WHEN try to load properties and all dependencies execute successfully THEN should return a List#Property`() {
        val propertyRepository = FakePropertyRepository
        val currencyRepository = FakeCurrencyRepository


        runTest {
            val useCase = LoadPropertiesUseCaseImpl(
                propertyRepository = propertyRepository,
                currencyRepository = currencyRepository,
            )

            val useCaseResult = useCase.execute()
            val properties = useCaseResult.getOrNull()

            assertTrue(useCaseResult.isSuccess)
            assertEquals(
                properties,
                listOf(
                    propertyRepository.fetchResultWithCurrencyRateApplied(
                        currencyRepository.getSelectedCurrency(),
                        currencyRepository.getCurrencies()
                    )
                )
            )
        }
    }

    @Test
    fun `WHEN try to load properties and currency dependency fails THEN should return a Result#Error`() {
        val propertyRepository = FakePropertyRepository
        val currencyRepository = object: CurrencyRepository {
            override suspend fun getCurrencies(): List<Currency> { throw Exception() }
            override fun getSelectedCurrency(): AppCurrency { throw Exception() }
            override fun setSelectedCurrency(newCurrency: AppCurrency, clearCache: Boolean) { throw Exception() }
            override fun getAvailableCurrencies(): List<AppCurrency> { throw Exception() }
        }


        runTest {
            val useCase = LoadPropertiesUseCaseImpl(
                propertyRepository = propertyRepository,
                currencyRepository = currencyRepository,
            )

            val useCaseResult = useCase.execute()
            assertTrue(useCaseResult.isFailure)
        }
    }

    @Test
    fun `WHEN try to load properties and PropertyRepository dependency fails THEN should return a Result#Error`() {
        runTest {
            val propertyRepository = object: PropertyRepository {
                override suspend fun fetch(): List<Property> { throw Exception() }
            }

            val currencyRepository = FakeCurrencyRepository

            val useCase = LoadPropertiesUseCaseImpl(
                propertyRepository = propertyRepository,
                currencyRepository = currencyRepository,
            )

            val useCaseResult = useCase.execute()
            assertTrue(useCaseResult.isFailure)
        }
    }
}