package com.pedronsouza.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetCurrenciesResponse(
    val base: String,
    val rates: Map<String, Double>,
)