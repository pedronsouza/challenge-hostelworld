package com.pedronsouza.data.internal

import kotlinx.serialization.json.Json

internal val DataJsonConfig = Json {
    prettyPrint = true
    isLenient = true
    coerceInputValues = true
    ignoreUnknownKeys = true
}

internal object Constants {
    const val GET_PROPERTIES_SERVICE = "https://gist.githubusercontent.com/PedroTrabulo-\n" +
            "Hostelworld/a1517b9da90dd6877385a65f324ffbc3/raw/058e83aa802907cb0032a15ca9054da563\n" +
            "\n" +
            "138541/properties.json"
}