package com.pedronsouza.data.mappers

import com.pedronsouza.data.responses.PropertyResponse
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property

class PropertyObjectMapper : ObjectMapper<List<PropertyResponse>, List<Property>>() {
    override fun transform(inputData: List<PropertyResponse>): List<Property> =
        inputData.map { item ->
            Property(
                id = item.id,
                name = item.name,
                lowestPriceByNight = item.lowestPricePerNight.value,
                rating = item.overallRating.overall,
                description = item.overview
            )
        }
}