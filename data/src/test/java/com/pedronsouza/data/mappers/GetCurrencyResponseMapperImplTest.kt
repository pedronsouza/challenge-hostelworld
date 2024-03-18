package com.pedronsouza.data.mappers

import com.pedronsouza.data.responses.GetCurrenciesResponse
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrencyResponseMapperImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Test
    fun `Given a valid GetCurrenciesResponse When trying to transform data Then should map data correctly to a Currency instance`() {
        val inputData = GetCurrenciesResponse(
            base = "EUR",
            rates = mapOf("EUR" to 1.0)
        )

        runTest(testDispatcher) {
            val subject = GetCurrencyResponseMapperImpl()
            assertEquals(
                listOf(Currency("EUR", 1.0)),
                subject.transform(inputData)
            )
        }
    }

    @Test
    fun `Given a invalid GetCurrenciesResponse When trying to transform data Then should return an empty list`() {
        val inputData = GetCurrenciesResponse(
            base = "EUR",
            rates = emptyMap()
        )

        runTest(testDispatcher) {
            val subject = GetCurrencyResponseMapperImpl()
            assertEquals(
                emptyList(),
                subject.transform(inputData)
            )
        }
    }
}