package com.pedronsouza.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetPropertiesResponse(
    val properties: List<PropertyResponse>,
    val location: PropertyLocationResponse
)