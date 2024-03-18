package com.pedronsouza.shared.extensions

import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.values.AppCurrency
import kotlin.test.Test
import kotlin.test.assertEquals

class PropertyExtTest {
    @Test
    fun `Given a valid currency When priceFormatted is called Then should return expected value`() {
        val expectedValue = "€23.90"
        assertEquals(expectedValue,
            FakeProperty.copy(
                lowestPriceByNightWithRateApplied = 23.9
            ).priceFormatted(AppCurrency("EUR"))
        )
    }
}