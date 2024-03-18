package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.fakes.FakePropertyItem

@Composable
fun PropertyContent(item: PropertyItem, cardMode: CardMode, modifier: Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (name, displayPrice, location, descriptionText) = createRefs()

        Text(
            text = item.name,
            fontWeight = FontWeight.Medium,
            fontSize = LocalDimensions.current.propertyCardNameTextSize,
            textAlign = TextAlign.Start,
            modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(displayPrice.start)
                width = Dimension.fillToConstraints
            }.testTag("property_list_card_name_${item.id}")
        )

        Text(
            text = item.location,
            textAlign = TextAlign.Start,
            color = LocalColors.current.darkGray,
            fontSize = LocalDimensions.current.propertyCardNameTextSize,
            modifier = Modifier.constrainAs(location) {
                top.linkTo(name.bottom)
                start.linkTo(parent.start)
                end.linkTo(displayPrice.start)
                width = Dimension.fillToConstraints
            }.testTag("property_list_card_location_${item.id}")
        )

        Text(
            text = item.displayPrice,
            textAlign = TextAlign.Center,
            color = LocalColors.current.lightGreen,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            modifier = Modifier.constrainAs(displayPrice) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(location.bottom)
            }.padding(
                start = LocalDimensions.current.defaultSpacingBetweenPropertyCards
            ).testTag("property_list_card_displayPrice_${item.id}")

        )
        
        item.description?.let { description ->
            val constraintScopeForDescription: ConstrainScope.() -> Unit = {
                top.linkTo(location.bottom)
                start.linkTo(parent.start)
            }
            when (cardMode) {
                CardMode.SHOWROOM ->
                    ExpandableText(
                        text = description,
                        fontSize = LocalDimensions.current.propertyCardDescriptionTextSize,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.constrainAs(
                            ref = descriptionText,
                            constrainBlock = constraintScopeForDescription
                        ).testTag("property_list_card_description_${item.id}")
                    )
                CardMode.CAROUSEL ->
                    Text(
                        text = description,
                        textAlign = TextAlign.Justify,
                        fontSize = LocalDimensions.current.propertyCardDescriptionTextSize,
                        modifier = Modifier.constrainAs(
                            ref = descriptionText,
                            constrainBlock = constraintScopeForDescription
                        ).testTag("property_list_card_description_${item.id}")
                    )
            }

        }
    }
}

@Preview
@Composable
fun previewPropertyContentCarousel() {
    PropertyContent(
        item = FakePropertyItem ,
        cardMode = CardMode.CAROUSEL,
        modifier = Modifier
    )
}

@Preview
@Composable
fun previewPropertyContent() {
    PropertyContent(
        item = FakePropertyItem ,
        cardMode = CardMode.CAROUSEL,
        modifier = Modifier
    )
}