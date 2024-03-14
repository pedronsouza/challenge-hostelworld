package com.pedronsouza.feature.property_list

import com.pedronsouza.domain.models.RemoteResource

data class PropertyListItem(
    val id: String,
    val name: String,
    val value: Double,
    val description: String?,
    val images: List<RemoteResource>
)