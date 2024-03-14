package com.pedronsouza.feature.property_list

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.pedronsouza.shared.components.models.PropertyListItem
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.json.Json

class PropertyListItemParamType : NavType<PropertyListItem>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): PropertyListItem? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, PropertyListItem::class.java)
        } else {
            bundle.getParcelable(key)
        }

    @OptIn(ExperimentalEncodingApi::class)
    override fun parseValue(value: String): PropertyListItem =
        Json.decodeFromString<PropertyListItem>(
            String(Base64.decode(value.toByteArray()))
        )

    override fun put(bundle: Bundle, key: String, value: PropertyListItem) {
        bundle.putParcelable(key, value)
    }
}