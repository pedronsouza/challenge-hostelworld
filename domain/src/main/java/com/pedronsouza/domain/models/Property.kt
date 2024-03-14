package com.pedronsouza.domain.models

import com.pedronsouza.domain.values.HtmlContent

data class Property(
    val id: String,
    val name: String,
    val lowestPriceByNight: Double,
    val rating: Int,
    val description: HtmlContent,
    val images: List<RemoteResource>
)
