package com.pedronsouza.shared.mappers

import com.pedronsouza.domain.ContentParser
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.values.HtmlContent
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.components.models.RatingCategory

interface PropertyListMapper : ObjectMapper<List<Property>, List<PropertyItem>>
internal class PropertyListMapperImpl(
    private val parser: ContentParser<HtmlContent, String>
) : PropertyListMapper {
    override fun transform(inputData: List<Property>) =
        inputData.map { item ->
            PropertyItem(
                id = item.name,
                name = item.name,
                value = item.lowestPriceByNight,
                images = item.images.map { it.toString() },
                description = parser.parse(item.description),
                rating = mapOf(
                    RatingCategory.OVERALL to item.rating.overall,
                    RatingCategory.SECURITY to item.rating.security,
                    RatingCategory.FACILITIES to item.rating.facilities,
                    RatingCategory.AVERAGE to item.rating.average,
                    RatingCategory.CLEAN to item.rating.clean,
                    RatingCategory.STAFF to item.rating.staff,
                    RatingCategory.LOCATION to item.rating.location
                ),
                address = item.addressSegments.joinToString { "," },
                location = "${item.location.city.name}, ${item.location.city.country}"
            )
        }
}