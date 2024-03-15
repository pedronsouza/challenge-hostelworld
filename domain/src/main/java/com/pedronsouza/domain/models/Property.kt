package com.pedronsouza.domain.models

import com.pedronsouza.domain.values.HtmlContent

data class Property(
    val id: String,
    val name: String,
    val lowestPriceByNight: Double,
    val rating: Rating,
    val description: HtmlContent,
    val addressSegments: List<String>,
    val images: List<RemoteResource>,
    val location: PropertyLocation,
    val isFeatured: Boolean
)
