package com.pedronsouza.feature.property_list

import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property

internal class PropertyListItemObjectMapper :
    ObjectMapper<List<Property>, List<PropertyListItem>>() {
    override fun transform(inputData: List<Property>) =
        inputData.map { item ->
            PropertyListItem(
                id = item.name,
                name = item.name,
                value = item.value
            )
        }
}