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
import com.google.accompanist.themeadapter.material.createMdcTheme

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
        LocalColors provides Colors(isDarkMode),

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

data class Colors(
    private val isDarkMode: Boolean = false,
    val white: Color = Color(0xffffffff),
    val lightGray: Color = Color(0x96ebebf5),
    val mediumGray: Color = Color(0xffaeaeb2),
    val darkGray: Color = Color(0xff3a3a3c),
    val purple: Color = Color(0xffbf5af2),
    val toolbarTextColor: Color = Color(0xff000000),
    val ratingTextColor: Color = Color(0xffff9f0A),
    val lightGreen: Color = Color(0xff32d74b)
)

data class Dimensions(
    val defaultScreenPadding: Dp = 14.dp,
    val propertyShowroomImageSize: Dp = 140.dp,
    val propertyDetailImageSize: Dp = 180.dp,
    val innerTextContentPropertyCardPadding: Dp = 6.dp,
    val defaultSpacingBetweenPropertyCards: Dp = 8.dp,
    val propertyCardNameTextSize: TextUnit = 14.sp,
    val propertyCardDescriptionTextSize: TextUnit = 14.sp,
    val ratingCardTextSize: TextUnit = 12.sp,
    val maxRatingCardTextSize: TextUnit = 12.sp,
    val featurePropertyTopMargin: Dp = 16.dp,
    val featurePropertyLabelMargin: Dp = 4.dp,
    val defaultCardElevation: Dp = 8.dp
)

val LocalDimensions = compositionLocalOf { Dimensions() }
val LocalColors = compositionLocalOf { Colors() }