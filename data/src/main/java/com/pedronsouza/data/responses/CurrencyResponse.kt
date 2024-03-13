package com.pedronsouza.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    val value: Double,
    val currency: String
)