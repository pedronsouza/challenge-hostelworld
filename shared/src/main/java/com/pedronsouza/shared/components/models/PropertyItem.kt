package com.pedronsouza.shared.components.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PropertyItem(
    val id: String,
    val name: String,
    val value: Double,
    val rating: Map<RatingCategory, Double>,
    val description: String?,
    val images: List<String>,
    val address: String,
    val location: String
) : Parcelable