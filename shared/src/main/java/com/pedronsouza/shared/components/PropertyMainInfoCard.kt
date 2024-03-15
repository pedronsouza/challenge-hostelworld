package com.pedronsouza.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.components.models.RatingCategory

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
            val (image, content, featured, rating) = createRefs()

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
                        end = LocalDimensions.current.innerTextContentPropertyCardPadding,
                        top = LocalDimensions.current.innerTextContentPropertyCardPadding
                    )
                    .constrainAs(rating) {
                        top.linkTo(image.bottom)
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
    PropertyMainInfoCard(
        item = PropertyItem(
            id = "test-id",
            name = "Lorem ipsum dolor sit amet, consectetur ",
            value = 58.99,
            images = listOf(
                "https://res.cloudinary.com/test-hostelworld-com/image/upload/f_auto,q_auto/v1/propertyimages/1/100/qzseav8zdfqpugqjpvlj",
            ),
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            rating = mapOf(
                RatingCategory.OVERALL to 8.3,
                RatingCategory.SECURITY to 7.5,
                RatingCategory.FACILITIES to 1.2,
                RatingCategory.AVERAGE to 9.1,
                RatingCategory.CLEAN to 6.7,
                RatingCategory.STAFF to 12.1,
                RatingCategory.LOCATION to 5.3
            ),
            address = "29 Bachelors Walk, Dublin 1",
            location = "Dublin, Ireland",
            isFeatured = true
        )
    )
}

@Preview
@Composable
fun previewPropertyMainInfoCardCarousel() {
    PropertyMainInfoCard(
        item = PropertyItem(
            id = "test-id",
            name = "Lorem ipsum dolor sit amet, consectetur ",
            value = 58.99,
            images = listOf(
                "https://res.cloudinary.com/test-hostelworld-com/image/upload/f_auto,q_auto/v1/propertyimages/1/100/qzseav8zdfqpugqjpvlj",
            ),
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            rating = mapOf(
                RatingCategory.OVERALL to 8.3,
                RatingCategory.SECURITY to 7.5,
                RatingCategory.FACILITIES to 1.2,
                RatingCategory.AVERAGE to 9.1,
                RatingCategory.CLEAN to 6.7,
                RatingCategory.STAFF to 12.1,
                RatingCategory.LOCATION to 5.3
            ),
            address = "29 Bachelors Walk, Dublin 1",
            location = "Dublin, Ireland",
            isFeatured = true
        ),

        cardMode = CardMode.CAROUSEL
    )
}