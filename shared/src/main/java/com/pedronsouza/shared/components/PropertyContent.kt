package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import com.pedronsouza.shared.components.models.PropertyItem

@Composable
fun PropertyContent(item: PropertyItem, cardMode: CardMode, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = item.name,
            fontWeight = FontWeight.Medium,
            fontSize = LocalDimensions.current.propertyCardNameTextSize
        )

        Row {
            Icon(
                painter = rememberVectorPainter(image = Icons.Outlined.LocationOn),
                contentDescription = null,
                tint = LocalColors.current.darkGray
            )

            Text(
                text = item.location,
                color = LocalColors.current.darkGray,
                fontSize = LocalDimensions.current.propertyCardNameTextSize
            )
        }

        Spacer(
            modifier = Modifier
                .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
        )

        item.description?.let { description ->
            when (cardMode) {
                CardMode.SHOWROOM ->
                    ExpandableText(
                        text = description,
                        fontSize = LocalDimensions.current.propertyCardDescriptionTextSize
                    )
                CardMode.CAROUSEL ->
                    Text(
                        text = description,
                        fontSize = LocalDimensions.current.propertyCardDescriptionTextSize
                    )
            }

        }
    }
}