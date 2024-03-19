package com.pedronsouza.feature.property_detail

import app.cash.turbine.test
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.useCases.GetPropertyByIdUseCase
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.fakes.FakePropertyItem
import com.pedronsouza.shared.mappers.PropertyListMapper
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class PropertyDetailViewModelTest {
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val getPropertyByIdUseCase = object: GetPropertyByIdUseCase {
        override suspend fun execute(propertyId: String): Result<Property> =
            Result.success(FakeProperty)
    }

    private val propertyListMapper = object: PropertyListMapper {
        override fun transform(inputData: List<Property>): List<PropertyItem> =
            listOf(FakePropertyItem)
    }

    @Test
    fun `Given event LoadProperty is sent with a valid propertyId When loading for properties Then state must contain loaded property info`() {
        runTest(testDispatcher) {
            val subject = PropertyDetailViewModel(
                propertyId = FakeProperty.id,
                getPropertyByIdUseCase = getPropertyByIdUseCase,
                propertyListMapper = propertyListMapper
            )

            subject.sendEvent(PropertyDetailEvent.LoadProperty)
            subject.viewState.test {
                val result = awaitItem()

                assertEquals(FakePropertyItem, result.propertyItem)
                assertFalse(result.isLoading)
                assertNull(result.error)
            }
        }
    }

    @Test
    fun `Given event LoadProperty is sent with a valid invalid data When loading for properties Then state must contain the error throwed`() {
        val expectedException = UnsupportedOperationException()
        runTest(testDispatcher) {
            val subject = PropertyDetailViewModel(
                propertyId = FakeProperty.id,
                getPropertyByIdUseCase = object: GetPropertyByIdUseCase {
                    override suspend fun execute(propertyId: String): Result<Property> =
                        Result.failure(expectedException)

                },
                propertyListMapper = propertyListMapper
            )

            subject.sendEvent(PropertyDetailEvent.LoadProperty)
            subject.viewState.test {
                val result = awaitItem()

                assertFalse(result.isLoading)
                assertEquals(expectedException, result.error)
            }
        }
    }
}