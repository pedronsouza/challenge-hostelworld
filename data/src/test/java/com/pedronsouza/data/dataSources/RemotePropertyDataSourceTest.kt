package com.pedronsouza.data.dataSources

import com.pedronsouza.data.api.PropertyApi
import com.pedronsouza.data.internal.ServicesFactory
import com.pedronsouza.data.mappers.PropertyMapper
import com.pedronsouza.data.responses.CurrencyResponse
import com.pedronsouza.data.responses.GetPropertiesResponse
import com.pedronsouza.data.responses.LocationCityResponse
import com.pedronsouza.data.responses.PropertyImageResponse
import com.pedronsouza.data.responses.PropertyLocationResponse
import com.pedronsouza.data.responses.PropertyRatingBreakdownResponse
import com.pedronsouza.data.responses.PropertyRatingResponse
import com.pedronsouza.data.responses.PropertyResponse
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class RemotePropertyDataSourceTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val getPropertiesResponse = GetPropertiesResponse(
        properties = listOf(
            PropertyResponse(
                id = FakeProperty.id,
                name = FakeProperty.name,
                overallRating = PropertyRatingResponse(1, 1),
                images = listOf(PropertyImageResponse("img-url", "test")),
                ratingBreakdown = PropertyRatingBreakdownResponse(
                    FakeProperty.rating.security,
                    FakeProperty.rating.location,
                    FakeProperty.rating.staff,
                    FakeProperty.rating.clean,
                    FakeProperty.rating.facilities,
                    FakeProperty.rating.average,
                    FakeProperty.rating.overall
                ),
                address1 = FakeProperty.addressSegments.first(),
                address2 = FakeProperty.addressSegments.last(),
                isFeatured = false,
                lowestPricePerNight = CurrencyResponse(
                    FakeProperty.lowestPriceByNight,
                    "EUR"
                ),
                overview = FakeProperty.description.toString()
            )
        ),

        location = PropertyLocationResponse(
            city = LocationCityResponse(
                id = "test-id",
                name = "Test City",
                country = "Test Country"
            )
        )
    )

    @Test
    fun `Given a valid instance of the dataSource When execute fetch Then should return Properties retrieved in network`() {
        val mapper = object: PropertyMapper {
            override fun transform(inputData: GetPropertiesResponse) =
                listOf(FakeProperty)
        }

        val api = object : PropertyApi {
            override suspend fun getProperties(): GetPropertiesResponse =
                getPropertiesResponse
        }

        val servicesFactory = object: ServicesFactory {
            override fun <T> getOrCreate(type: KClass<*>): T =
                api as T
        }

        val subject = RemotePropertyDataSource(servicesFactory, mapper)

        runTest(testDispatcher) {
            val data = subject.fetch()
            assertEquals(listOf(FakeProperty), data)
        }
    }

    @Test
    fun `Given the api is unavailable When execute fetch Then should return Properties retrieved in network`() {
        val exception = UnsupportedOperationException()
        val mapper = object: PropertyMapper {
            override fun transform(inputData: GetPropertiesResponse) =
                listOf(FakeProperty)
        }

        val api = object : PropertyApi {
            override suspend fun getProperties(): GetPropertiesResponse =
                throw exception
        }

        val servicesFactory = object: ServicesFactory {
            override fun <T> getOrCreate(type: KClass<*>): T =
                api as T
        }

        val subject = RemotePropertyDataSource(servicesFactory, mapper)

        runTest(testDispatcher) {
            assertFailsWith<UnsupportedOperationException> {
                subject.fetch()
            }
        }
    }

    @Test
    fun `Given the servicesFactory doesnt find the API When execute fetch Then should return Properties retrieved in network`() {
        val exception = UnsupportedOperationException()
        val mapper = object: PropertyMapper {
            override fun transform(inputData: GetPropertiesResponse) =
                listOf(FakeProperty)
        }

        val api = object : PropertyApi {
            override suspend fun getProperties(): GetPropertiesResponse =
                throw exception
        }

        val servicesFactory = object: ServicesFactory {
            override fun <T> getOrCreate(type: KClass<*>): T =
                api as T
        }

        val subject = RemotePropertyDataSource(servicesFactory, mapper)

        runTest(testDispatcher) {
            assertFailsWith<UnsupportedOperationException> {
                subject.fetch()
            }
        }
    }

    @Test
    fun `Given the mapper fails  When execute fetch Then should return Properties retrieved in network`() {
        val exception = UnsupportedOperationException()
        val mapper = object: PropertyMapper {
            override fun transform(inputData: GetPropertiesResponse) =
                throw exception
        }

        val api = object : PropertyApi {
            override suspend fun getProperties(): GetPropertiesResponse =
                getPropertiesResponse
        }

        val servicesFactory = object: ServicesFactory {
            override fun <T> getOrCreate(type: KClass<*>): T =
                api as T
        }

        val subject = RemotePropertyDataSource(servicesFactory, mapper)

        runTest(testDispatcher) {
            assertFailsWith<UnsupportedOperationException> {
                subject.fetch()
            }
        }
    }
}