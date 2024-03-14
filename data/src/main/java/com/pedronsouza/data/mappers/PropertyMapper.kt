package com.pedronsouza.data.mappers

import com.pedronsouza.data.internal.Constants.ImagesUrlProtocol
import com.pedronsouza.data.responses.GetPropertiesResponse
import com.pedronsouza.data.responses.PropertyImageResponse
import com.pedronsouza.data.responses.PropertyLocationResponse
import com.pedronsouza.data.responses.PropertyRatingBreakdownResponse
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.City
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.models.PropertyLocation
import com.pedronsouza.domain.models.Rating
import com.pedronsouza.domain.models.RemoteResource
import com.pedronsouza.domain.values.HtmlContent

interface PropertyMapper : ObjectMapper<GetPropertiesResponse, List<Property>>
internal class PropertyObjectMapper :
    PropertyMapper {
    override fun transform(inputData: GetPropertiesResponse): List<Property> =
        inputData.properties.map { item ->
            Property(
                id = item.id,
                name = item.name,
                lowestPriceByNight = item.lowestPricePerNight.value,
                description = HtmlContent(item.overview),
                images = item.images.toRemoteResource(),
                rating = item.ratingBreakdown.toRating(),
                addressSegments = listOf(item.address1, item.address2),
                location = inputData.location.toPropertyLocation()
            )
        }

    private fun PropertyLocationResponse.toPropertyLocation() =
        PropertyLocation(
            city = City(
                id = this.city.id,
                name = this.city.name,
                country = this.city.country
            )
        )

    private fun PropertyRatingBreakdownResponse.toRating() =
        Rating(
            security = security,
            location = location,
            staff = staff,
            clean = clean,
            facilities = facilities,
            average = average,
            overall = overall
        )

    private fun List<PropertyImageResponse>.toRemoteResource() =
        map { imageResponse ->
            RemoteResource(
                url = "$ImagesUrlProtocol://${imageResponse.prefix}${imageResponse.suffix}"
            )
        }
}