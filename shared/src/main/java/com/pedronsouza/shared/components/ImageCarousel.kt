package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ImageCarousel(images: List<String>) {
    LazyRow {
        items(images) {imageUrl ->
            Card {
                RemoteImage(
                    url = imageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalDimensions.current.propertyDetailImageSize)
                )
            }

            Spacer(modifier = Modifier.width(LocalDimensions.current.defaultSpacingBetweenPropertyCards))
        }
    }
}