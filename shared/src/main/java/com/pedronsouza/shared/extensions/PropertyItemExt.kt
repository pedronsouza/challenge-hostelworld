package com.pedronsouza.shared.extensions

import com.pedronsouza.shared.components.models.PropertyItem
import java.text.DecimalFormat

internal fun PropertyItem.priceFormatted() =
    try {
        DecimalFormat("#,###.00").run {
            isDecimalSeparatorAlwaysShown = false
            format(value)
        }
    } catch (e: ArithmeticException) {
        value.toString()
    }