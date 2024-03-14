package com.pedronsouza.shared.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import com.pedronsouza.shared.components.models.PropertyItem

enum class CardMode {
    SHOWROOM,
    CAROUSEL
}

@Composable
fun PropertyMainInfoCard(
    item: PropertyItem,
    cardMode: CardMode = CardMode.SHOWROOM
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ConstraintLayout {
            val (image, content, rating) = createRefs()

            val imageConstraint: ConstrainScope.() -> Unit = {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }

            if (cardMode == CardMode.SHOWROOM) {
                RemoteImage(
                    url = item.images.first().toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalDimensions.current.propertyShowroomImageSize)
                        .constrainAs(image, imageConstraint)
                )
            } else {
                ImageCarousel(item.images, modifier = Modifier.constrainAs(image, imageConstraint))
            }

            PropertyContent(
                item = item,
                cardMode = cardMode,
                modifier = Modifier
                    .padding(LocalDimensions.current.innerTextContentPropertyCardPadding)
                    .constrainAs(content) {
                        top.linkTo(image.bottom)
                        start.linkTo(parent.start)
                    }
            )

            PropertyRating(
                ratings = item.rating,
                modifier = Modifier
                    .padding(
                        end = LocalDimensions.current.innerTextContentPropertyCardPadding,
                        top = LocalDimensions.current.innerTextContentPropertyCardPadding
                    )
                    .constrainAs(rating) {
                        top.linkTo(image.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}