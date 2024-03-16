package com.pedronsouza.domain

import com.pedronsouza.domain.exceptions.CurrencyNotAvailableException
import com.pedronsouza.domain.fakes.FakeCurrencyRepository
import com.pedronsouza.domain.fakes.FakePropertyRepository
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.values.AppCurrency
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PropertyPriceCalculatorTest {
    @Test
    fun `GIVEN a valid currency that is available WHEN try to calculate the new value THEN should return a property with the correct rate applied`() {

        runTest {
            val availableCurrencies = FakeCurrencyRepository.getCurrencies()
            val validCurrency = AppCurrency(availableCurrencies.first().currencyCode)

            val property = FakePropertyRepository.fetch().first()
            val expectedCurrencyRate = availableCurrencies.first()

            val expectedPriceWithRate = property.lowestPriceByNight * expectedCurrencyRate.rate

            assertEquals(
                expectedPriceWithRate,
                PropertyPriceCalculator.prepareValueWithCurrencyRateApplied(
                    property = property,
                    currencies = availableCurrencies,
                    selectedCurrency = validCurrency
                ).lowestPriceByNightWithRateApplied
            )
        }
    }

    @Test
    fun `GIVEN a currency that is not available WHEN try to calculate the new value THEN it should throws a CurrencyNotAvailableException`() {
        val availableCurrencies = emptyList<Currency>()
        val invalidCurrency = AppCurrency("unknown")

        assertFailsWith<CurrencyNotAvailableException> {
            PropertyPriceCalculator.prepareValueWithCurrencyRateApplied(
                FakeProperty,
                availableCurrencies,
                invalidCurrency
            )
        }
    }
}