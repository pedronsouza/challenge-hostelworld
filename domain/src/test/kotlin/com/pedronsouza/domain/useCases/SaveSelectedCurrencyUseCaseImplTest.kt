package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.fakes.FakeCurrencyRepository
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.repositories.CurrencyRepository
import com.pedronsouza.domain.values.AppCurrency
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SaveSelectedCurrencyUseCaseImplTest {
    @Test
    fun `GIVEN that dependencies work as expected WHEN executing the useCase THEN it must return a Result with success`() {
        runTest {
            val currencyRepository = FakeCurrencyRepository
            val useCase = SaveSelectedCurrencyUseCaseImpl(currencyRepository)
            val useCaseResult = useCase.execute(currencyRepository.getSelectedCurrency())

            assertTrue(useCaseResult.isSuccess)
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

            val useCase = SaveSelectedCurrencyUseCaseImpl(currencyRepository)
            val useCaseResult = useCase.execute(FakeCurrencyRepository.getSelectedCurrency())

            assertTrue(useCaseResult.isFailure)
        }
    }
}