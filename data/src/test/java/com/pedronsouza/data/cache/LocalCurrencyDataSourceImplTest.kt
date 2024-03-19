package com.pedronsouza.data.cache

import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.values.AppCurrency
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class LocalCurrencyDataSourceImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Test
    fun `Given cache is not warmed up When adding a new list of currencies Then should save in cache and flag it as warmed up `() {
        val subject = LocalCurrencyDataSourceImpl()
        assertFalse(subject.isWarmed())

        runTest(testDispatcher) {
            subject.addToCache(listOf(Currency("EUR", 1.0)))
            subject.getCurrencies()
            assertTrue(subject.isWarmed())
        }
    }

    @Test
    fun `Given a new valid currency When saving as selected Then should switch cache instance with the new currency `() {
        val subject = LocalCurrencyDataSourceImpl()
        val expectedCurrency = AppCurrency("TST")

        assertNotEquals(expectedCurrency, subject.getSelectedCurrency())

        runTest(testDispatcher) {
            subject.setSelectedCurrency(expectedCurrency, false)

            assertEquals(expectedCurrency, subject.getSelectedCurrency())
        }
    }
}