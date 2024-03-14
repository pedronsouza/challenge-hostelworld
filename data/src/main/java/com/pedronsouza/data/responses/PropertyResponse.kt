package com.pedronsouza.data.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyResponse(
    val id: String,
    val name: String,
    val overallRating: PropertyRatingResponse,
    val lowestPricePerNight: CurrencyResponse,
    val overview: String,
    @SerialName("imagesGallery") val images: List<PropertyImageResponse>,
    val ratingBreakdown: PropertyRatingBreakdownResponse,
    val address1: String,
    val address2: String
)