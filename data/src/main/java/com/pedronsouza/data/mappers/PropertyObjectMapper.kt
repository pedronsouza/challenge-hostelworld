package com.pedronsouza.data.mappers

import com.pedronsouza.data.internal.Constants.ImagesUrlProtocol
import com.pedronsouza.data.responses.PropertyImageResponse
import com.pedronsouza.data.responses.PropertyResponse
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.models.RemoteResource
import com.pedronsouza.domain.values.HtmlContent

class PropertyObjectMapper : ObjectMapper<List<PropertyResponse>, List<Property>>() {
    override fun transform(inputData: List<PropertyResponse>): List<Property> =
        inputData.map { item ->
            Property(
                id = item.id,
                name = item.name,
                lowestPriceByNight = item.lowestPricePerNight.value,
                rating = item.overallRating.overall,
                description = HtmlContent(item.overview),
                images = item.images.toRemoteResource()
            )
        }

    private fun List<PropertyImageResponse>.toRemoteResource() =
        map { imageResponse ->
            RemoteResource(
                url = "$ImagesUrlProtocol://${imageResponse.prefix}${imageResponse.suffix}"
            )
        }
}