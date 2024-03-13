package com.pedronsouza.challenge

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.createMdcTheme

@Composable
fun AppTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val (colors, typography, shapes) = createMdcTheme(
        context = context,
        layoutDirection = layoutDirection
    )

    CompositionLocalProvider(
        // Colors
        LocalColors provides LightColors(),

        // Dimensions
        LocalDimensions provides Dimensions(),
    ) {
        MaterialTheme(
            colors = colors ?: MaterialTheme.colors,
            typography = typography ?: MaterialTheme.typography,
            shapes = shapes ?: MaterialTheme.shapes,
            content = content
        )
    }
}

interface AppColor {
    val defaultScreenBg: Color
}

data class LightColors(
    override val defaultScreenBg: Color = Color(0xFFFFFF)
) : AppColor

data class DarkColors(
    override val defaultScreenBg: Color
) : AppColor

data class Dimensions(val defaultScreenPadding: Dp = 14.dp)



val LocalDimensions = compositionLocalOf { Dimensions() }
val LocalColors = compositionLocalOf { LightColors() }