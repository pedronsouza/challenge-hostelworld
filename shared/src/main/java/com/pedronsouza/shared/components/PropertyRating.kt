package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pedronsouza.shared.components.models.RatingCategory

@Composable
fun PropertyRating(ratings: Map<RatingCategory, Double>, modifier: Modifier) {
    LazyRow(modifier = modifier) {
        items(ratings.toList()) { (category, rating) ->
            val iconVector = enabledCategoryToIcon(category)
            if (iconVector != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = rememberVectorPainter(image = iconVector),
                        contentDescription = null,
                    )
                    Text(
                        text = category.name
                            .toLowerCase(Locale.current)
                            .capitalize(Locale.current),
                        fontWeight = FontWeight.Medium,
                        fontSize = LocalDimensions.current.ratingCardTextSize,
                        lineHeight = 1.sp
                    )
                    Text(
                        text = "${rating}/10",
                        fontWeight = FontWeight.Medium,
                        fontSize = LocalDimensions.current.ratingCardTextSize,
                        lineHeight = 1.sp
                    )
                }

                Spacer(
                    modifier = Modifier.width(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
                )
            }
        }
    }
}

private fun enabledCategoryToIcon(category: RatingCategory) =
    when(category) {
        RatingCategory.SECURITY -> Icons.Outlined.Lock
        RatingCategory.LOCATION -> Icons.Outlined.LocationOn
        RatingCategory.STAFF -> Icons.Outlined.Person
        RatingCategory.CLEAN -> Icons.Outlined.Delete
        RatingCategory.FACILITIES -> Icons.Outlined.Home
        else -> null
    }


@Preview
@Composable
fun previewPropertyRating() {
    PropertyRating(
        ratings = RatingCategory.entries.associateWith { 8.3 },
        modifier = Modifier
    )
}