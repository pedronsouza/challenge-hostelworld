package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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