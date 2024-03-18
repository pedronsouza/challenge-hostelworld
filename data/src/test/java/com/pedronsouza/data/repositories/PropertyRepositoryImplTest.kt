package com.pedronsouza.data.repositories

import com.pedronsouza.domain.dataSources.PropertyDataSource
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.Property
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class PropertyRepositoryImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Test
    fun `Given a valid instance of the repository When fetching for results Then it should return a list of the properties retrieved from Server`() {
        val fakeDataSource = object: PropertyDataSource {
            override suspend fun fetch(): List<Property> =
                listOf(FakeProperty)
        }

        val subject = PropertyRepositoryImpl(fakeDataSource)

        runTest(testDispatcher) {
            assertEquals(listOf(FakeProperty), subject.fetch())
        }
    }

    @Test
    fun `Given a instance of the repository When fetching for results with a invaliad dataSource Then it should return a list of the properties retrieved from Server`() {
        val fakeDataSource = object: PropertyDataSource {
            override suspend fun fetch(): List<Property> =
                throw UnsupportedOperationException()
        }

        val subject = PropertyRepositoryImpl(fakeDataSource)

        runTest(testDispatcher) {
            assertFailsWith<UnsupportedOperationException> {
                subject.fetch()
            }
        }
    }
}