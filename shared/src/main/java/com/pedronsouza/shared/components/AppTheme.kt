package com.pedronsouza.shared.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val toolbarBg: Color
    val toolbarTextColor: Color
    val ratingTextColor: Color
}

data class LightColors(
    override val defaultScreenBg: Color = Color(0xffffffff),
    override val toolbarBg: Color = Color(0x96ebebf5),
    override val toolbarTextColor: Color = Color(0xff000000),
    override val ratingTextColor: Color = Color(0xffff9f0A)
) : AppColor

data class DarkColors(
    override val defaultScreenBg: Color,
    override val toolbarBg: Color = Color(0x96ebebf5),
    override val toolbarTextColor: Color = Color(0xffffffff),
    override val ratingTextColor: Color = Color(0xffff9f0A)
) : AppColor

data class Dimensions(
    val defaultScreenPadding: Dp = 14.dp,
    val propertyShowroomImageSize: Dp = 140.dp,
    val propertyDetailImageSize: Dp = 180.dp,
    val innerTextContentPropertyCardPadding: Dp = 6.dp,
    val defaultSpacingBetweenPropertyCards: Dp = 8.dp,
    val propertyCardNameTextSize: TextUnit = 14.sp,
    val propertyCardDescriptionTextSize: TextUnit = 14.sp,
    val ratingCardTextSize: TextUnit = 14.sp,
    val maxRatingCardTextSize: TextUnit = 12.sp
)

val LocalDimensions = compositionLocalOf { Dimensions() }
val LocalColors = compositionLocalOf { LightColors() }