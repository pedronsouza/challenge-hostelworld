package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.pedronsouza.shared.components.models.PropertyItem


enum class ImageMode {
    SHOWROOM,
    CAROUSEL
}

@Composable
fun PropertyMainInfoCard(
    item: PropertyItem,
    imageMode: ImageMode = ImageMode.SHOWROOM
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (imageMode == ImageMode.SHOWROOM) {
            RemoteImage(
                url = item.images.first().toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalDimensions.current.propertyShowroomImageSize)
            )
        } else {
            ImageCarousel(item.images)
        }

        Column(modifier = Modifier.padding(LocalDimensions.current.innerTextContentPropertyCardPadding)) {
            Spacer(
                modifier = Modifier
                    .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
            )

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
                Text(
                    text = description,
                    fontSize = LocalDimensions.current.propertyCardDescriptionTextSize
                )
            }
        }
    }
}
