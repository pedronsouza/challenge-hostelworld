package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.Constant
import com.pedronsouza.domain.fakes.FakeCurrencyRepository
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class GetAvailableCurrenciesUseCaseImplTest {
    @Test
    fun `GIVEN that dependencies work as expected WHEN executing the useCase THEN it must return a Result with the local available currencies`() {
        runTest {
            val currencyRepository = FakeCurrencyRepository
            val useCase = GetAvailableCurrenciesUseCaseImpl(currencyRepository)
            val useCaseResult = useCase.execute()

            assertTrue(useCaseResult.isSuccess)
            assertEquals(Constant.availableCurrencies, useCaseResult.getOrThrow())
        }
    }

    @Test
    fun `GIVEN that currencyRepository does not work as expected WHEN executing the useCase THEN it must return a Result with the error`() {
        runTest {
            val currencyRepository = object: CurrencyRepository {
                override suspend fun getCurrencies(): List<Currency> { TODO("Not yet implemented") }
                override fun getSelectedCurrency(): AppCurrency { TODO("Not yet implemented") }
                override fun setSelectedCurrency(newCurrency: AppCurrency) { TODO("Not yet implemented") }
                override fun getAvailableCurrencies(): List<AppCurrency> { TODO("Not yet implemented") }

            }

            val useCase = GetAvailableCurrenciesUseCaseImpl(currencyRepository)
            val useCaseResult = useCase.execute()

            assertTrue(useCaseResult.isFailure)
        }
    }
}