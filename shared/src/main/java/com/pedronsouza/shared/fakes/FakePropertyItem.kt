package com.pedronsouza.shared.fakes

import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.components.models.RatingCategory
import java.util.Currency

val FakePropertyItem = PropertyItem(
    id = FakeProperty.id,
    name = FakeProperty.name,
    lowestPriceByNight = FakeProperty.lowestPriceByNight,
    images = FakeProperty.images.map { it.toString() },
    description = FakeProperty.description.toString(),
    rating = mapOf(
        RatingCategory.OVERALL to 8.3,
        RatingCategory.SECURITY to 8.3,
        RatingCategory.FACILITIES to 8.3,
        RatingCategory.AVERAGE to 8.3,
        RatingCategory.CLEAN to 8.3,
        RatingCategory.STAFF to 8.3,
        RatingCategory.LOCATION to 8.3
    ),
    address = FakeProperty.addressSegments.joinToString { ", " },
    location = "${FakeProperty.location.city.name}, ${FakeProperty.location.city.country}",
    isFeatured = true,
    displayPrice = FakeProperty.lowestPriceByNightWithRateApplied.toString()
)

