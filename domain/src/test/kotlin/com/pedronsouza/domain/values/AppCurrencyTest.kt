package com.pedronsouza.domain.values

import java.util.Currency
import kotlin.test.Test
import kotlin.test.assertEquals

class AppCurrencyTest {
    @Test
    fun `Given a valid currencyCode to the instance When ask for displayName THEN should return expected name from system`() {
        val validCurrency = AppCurrency("EUR")
        val expectedName = Currency.getInstance(validCurrency.toString()).displayName
        assertEquals(expectedName, validCurrency.displayName)
    }
}