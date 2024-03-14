package com.pedronsouza.shared.components.models

import android.os.Parcelable
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.models.RemoteResource
import com.pedronsouza.domain.values.HtmlContent
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PropertyListItem(
    val id: String,
    val name: String,
    val value: Double,
    val rating: Int,
    val description: String?,
    val images: List<String>
) : Parcelable

fun PropertyListItem.toProperty() =
    Property(
        id = id,
        name = name,
        lowestPriceByNight = value,
        rating = rating,
        description = HtmlContent(description.orEmpty()),
        images = images.map { RemoteResource(it) }
    )