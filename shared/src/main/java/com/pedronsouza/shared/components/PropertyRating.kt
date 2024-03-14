package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pedronsouza.shared.components.models.RatingCategory

@Composable
fun PropertyRating(ratings: Map<RatingCategory, Double>, modifier: Modifier) {
    val overallRating = ratings[RatingCategory.OVERALL]

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.Star),
            contentDescription = null,
            tint = LocalColors.current.ratingTextColor
        )
        Text(
            text = "${overallRating.toString()}/10",
            fontWeight = FontWeight.Medium,
            color = LocalColors.current.ratingTextColor,
            fontSize = LocalDimensions.current.ratingCardTextSize,
            modifier = Modifier.padding(start = LocalDimensions.current.defaultSpacingBetweenPropertyCards),
            lineHeight = 1.sp
        )
    }
}