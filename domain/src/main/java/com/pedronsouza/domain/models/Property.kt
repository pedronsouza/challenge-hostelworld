package com.pedronsouza.domain.models

data class Property(
    val id: String,
    val name: String,
    val lowestPriceByNight: Double,
    val rating: Int,
    val description: String,
    val images: List<RemoteResource>
)
