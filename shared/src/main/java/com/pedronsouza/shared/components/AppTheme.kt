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
    val white: Color
    val lightGray: Color
    val mediumGray: Color
    val toolbarTextColor: Color
    val ratingTextColor: Color
    val darkGray: Color
    val purple: Color
}

data class LightColors(
    override val white: Color = Color(0xffffffff),
    override val lightGray: Color = Color(0x96ebebf5),
    override val mediumGray: Color = Color(0xffaeaeb2),
    override val darkGray: Color = Color(0xff3a3a3c),
    override val purple: Color = Color(0xffbf5af2),
    override val toolbarTextColor: Color = Color(0xff000000),
    override val ratingTextColor: Color = Color(0xffff9f0A)
) : AppColor

data class DarkColors(
    override val white: Color,
    override val lightGray: Color = Color(0x96ebebf5),
    override val mediumGray: Color = Color(0xffaeaeb2),
    override val darkGray: Color = Color(0xff3a3a3c),
    override val purple: Color = Color(0xffbf5af2),
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
    val ratingCardTextSize: TextUnit = 12.sp,
    val maxRatingCardTextSize: TextUnit = 12.sp,
    val featurePropertyTopMargin: Dp = 16.dp,
    val featurePropertyLabelMargin: Dp = 4.dp
)

val LocalDimensions = compositionLocalOf { Dimensions() }
val LocalColors = compositionLocalOf { LightColors() }