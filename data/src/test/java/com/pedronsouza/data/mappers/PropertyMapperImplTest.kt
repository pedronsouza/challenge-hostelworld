package com.pedronsouza.data.mappers

import com.pedronsouza.data.fakes.FakePropertyImageResponse
import com.pedronsouza.data.internal.Constants.ImagesUrlProtocol
import com.pedronsouza.data.responses.CurrencyResponse
import com.pedronsouza.data.responses.GetPropertiesResponse
import com.pedronsouza.data.responses.LocationCityResponse
import com.pedronsouza.data.responses.PropertyLocationResponse
import com.pedronsouza.data.responses.PropertyRatingBreakdownResponse
import com.pedronsouza.data.responses.PropertyRatingResponse
import com.pedronsouza.data.responses.PropertyResponse
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.RemoteResource
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class PropertyMapperImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val getPropertiesResponse = GetPropertiesResponse(
        properties = listOf(
            PropertyResponse(
                id = FakeProperty.id,
                name = FakeProperty.name,
                overallRating = PropertyRatingResponse(1, 1),
                images = listOf(FakePropertyImageResponse),
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
                id = "test-location",
                name = "Dublin",
                country = "Ireland"
            )
        )
    )

    @Test
    fun `Given a valid GetPropertiesResponse When trying to transform data Then it should return a valid property list`() {
        val subject = PropertyMapperImpl()
        runTest(testDispatcher) {
            val result = subject.transform(getPropertiesResponse)
            assertEquals(
                listOf(
                    FakeProperty.copy(
                        images = listOf(
                            RemoteResource(
                                url = "$ImagesUrlProtocol://${FakePropertyImageResponse.prefix + FakePropertyImageResponse.suffix}")
                        )
                    )
                ), result
            )
        }
    }
}