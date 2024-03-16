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
        RatingCategory.SECURITY to 7.5,
        RatingCategory.FACILITIES to 1.2,
        RatingCategory.AVERAGE to 9.1,
        RatingCategory.CLEAN to 6.7,
        RatingCategory.STAFF to 12.1,
        RatingCategory.LOCATION to 5.3
    ),
    address = FakeProperty.addressSegments.joinToString { ", " },
    location = "${FakeProperty.location.city.name}, ${FakeProperty.location.city.country}",
    isFeatured = true,
    displayPrice = FakeProperty.lowestPriceByNightWithRateApplied.toString()
)

