package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import com.pedronsouza.shared.R
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.components.models.RatingCategory


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
        ConstraintLayout {
            val (image, content, rating) = createRefs()

            val imageConstraint: ConstrainScope.() -> Unit = {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }

            if (imageMode == ImageMode.SHOWROOM) {
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

            Column(
                modifier = Modifier
                    .padding(LocalDimensions.current.innerTextContentPropertyCardPadding)
                    .constrainAs(content) {
                        top.linkTo(image.bottom)
                        start.linkTo(parent.start)
                    }
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
                    Text(
                        text = description,
                        fontSize = LocalDimensions.current.propertyCardDescriptionTextSize
                    )
                }
            }

            PropertyRating(
                ratings = item.rating,
                modifier = Modifier.constrainAs(rating) {
                    top.linkTo(image.bottom)
                    start.linkTo(content.end)
                }
            )
        }
    }
}


@Composable
private fun PropertyRating(ratings: Map<RatingCategory, Int>, modifier: Modifier) {
    val overallRating = ratings[RatingCategory.OVERALL]
    val overallRatingLabel = if (overallRating != -1) {
        stringResource(id = R.string.no_ratings)
    } else {
        overallRating.toString()
    }

    Text(
        text = overallRatingLabel,
        fontWeight = FontWeight.Medium,
        color = LocalColors.current.ratingTextColor,
        fontSize = LocalDimensions.current.ratingCardTextSize
    )
}