package com.pedronsouza.shared.fakes

import com.pedronsouza.domain.models.City
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.models.PropertyLocation
import com.pedronsouza.domain.models.Rating
import com.pedronsouza.domain.models.RemoteResource
import com.pedronsouza.domain.values.HtmlContent
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.components.models.RatingCategory

val FakePropertyItem = PropertyItem(
    id = "test-id",
    name = "Lorem ipsum dolor sit amet, consectetur ",
    lowestPriceByNight = 58.99,
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
    isFeatured = true,
    displayPrice = "58,99 €"
)

val FakeProperty = Property(
    id = FakePropertyItem.id,
    name = FakePropertyItem.name,
    lowestPriceByNight = FakePropertyItem.lowestPriceByNight,
    rating = Rating(
        security = 83,
        overall = 83,
        facilities = 83,
        average = 83,
        clean = 83,
        staff = 83,
        location = 83
    ),
    description = HtmlContent(FakePropertyItem.description.orEmpty()),
    addressSegments = FakePropertyItem.address.split(", "),
    images = FakePropertyItem.images.map { RemoteResource(it) },
    location = PropertyLocation(
        city = City(
            id = "test-location",
            name = FakePropertyItem.location.split(", ").get(0).orEmpty(),
            country = FakePropertyItem.location.split(", ").get(1).orEmpty()
        )
    ),
    isFeatured = false
)