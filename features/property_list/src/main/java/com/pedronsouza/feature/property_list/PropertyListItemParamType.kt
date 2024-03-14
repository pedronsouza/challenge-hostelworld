package com.pedronsouza.feature.property_list

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.pedronsouza.shared.components.models.PropertyItem
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.json.Json

class PropertyListItemParamType : NavType<PropertyItem>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): PropertyItem? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, PropertyItem::class.java)
        } else {
            bundle.getParcelable(key)
        }

    @OptIn(ExperimentalEncodingApi::class)
    override fun parseValue(value: String): PropertyItem =
        Json.decodeFromString<PropertyItem>(
            String(Base64.decode(value.toByteArray()))
        )

    override fun put(bundle: Bundle, key: String, value: PropertyItem) {
        bundle.putParcelable(key, value)
    }
}