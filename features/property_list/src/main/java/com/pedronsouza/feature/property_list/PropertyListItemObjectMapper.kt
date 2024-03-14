package com.pedronsouza.feature.property_list

import com.pedronsouza.domain.ContentParser
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.values.HtmlContent

internal class PropertyListItemObjectMapper(
    private val parser: ContentParser<HtmlContent, String>
) :
    ObjectMapper<List<Property>, List<PropertyListItem>>() {
    override fun transform(inputData: List<Property>) =
        inputData.map { item ->
            PropertyListItem(
                id = item.name,
                name = item.name,
                value = item.lowestPriceByNight,
                images = item.images,
                description = parser.parse(item.description),
                rating = item.rating
            )
        }
}