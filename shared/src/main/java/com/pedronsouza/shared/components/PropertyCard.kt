package com.pedronsouza.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.fakes.FakePropertyItem

enum class CardMode {
    SHOWROOM,
    CAROUSEL
}

@Composable
fun PropertyCard(
    item: PropertyItem,
    cardMode: CardMode = CardMode.SHOWROOM
) {
    Card(
        elevation = LocalDimensions.current.defaultCardElevation,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ConstraintLayout {
            val (image, content, featured, rating, pricing) = createRefs()

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
                        start.linkTo(image.start)
                    }
            )

            PropertyRating(
                ratings = item.rating,
                modifier = Modifier
                    .padding(
                        LocalDimensions.current.innerTextContentPropertyCardPadding
                    )
                    .constrainAs(rating) {
                        top.linkTo(content.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            if (item.isFeatured) {
                FeaturedProperty(
                    modifier = Modifier.constrainAs(featured) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                        .padding(top = LocalDimensions.current.featurePropertyTopMargin)
                )
            }
        }
    }
}

@Composable
fun FeaturedProperty(modifier: Modifier) {
    Box(
        modifier = modifier
            .background(LocalColors.current.purple, shape = RoundedCornerShape(0.dp, 15.dp, 15.dp, 0.dp))

    ) {
        Text(
            text = "Featured Property",
            color = LocalColors.current.white,
            modifier = Modifier.padding(
                start = LocalDimensions.current.featurePropertyLabelMargin,
                end = LocalDimensions.current.featurePropertyLabelMargin
            ),
            fontSize = 10.sp
        )
    }
}


@Preview
@Composable
fun previewPropertyMainInfoCardShowRoom() {
    PropertyCard(
        item = FakePropertyItem.copy(isFeatured = false)
    )
}

@Preview
@Composable
fun previewPropertyMainInfoCardCarousel() {
    PropertyCard(
        item = FakePropertyItem,
        cardMode = CardMode.CAROUSEL
    )
}