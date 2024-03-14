package com.pedronsouza.shared.mappers

import com.pedronsouza.domain.ContentParser
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.values.HtmlContent
import com.pedronsouza.shared.components.models.PropertyItem

internal class PropertyListItemObjectMapper(
    private val parser: ContentParser<HtmlContent, String>
) :
    ObjectMapper<List<Property>, List<PropertyItem>>() {
    override fun transform(inputData: List<Property>) =
        inputData.map { item ->
            PropertyItem(
                id = item.name,
                name = item.name,
                value = item.lowestPriceByNight,
                images = item.images.map { it.toString() },
                description = parser.parse(item.description),
                rating = item.rating
            )
        }
}