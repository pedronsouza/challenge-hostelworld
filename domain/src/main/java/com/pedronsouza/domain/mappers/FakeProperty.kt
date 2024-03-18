package com.pedronsouza.domain.mappers

import com.pedronsouza.domain.models.City
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.models.PropertyLocation
import com.pedronsouza.domain.models.Rating
import com.pedronsouza.domain.models.RemoteResource
import com.pedronsouza.domain.values.HtmlContent

val FakeProperty = Property(
    id = "test-id",
    name = "Lorem ipsum dolor sit amet, consectetur ",
    lowestPriceByNight = 58.99,
    rating = Rating(
        security = 83,
        overall = 83,
        facilities = 83,
        average = 83,
        clean = 83,
        staff = 83,
        location = 83
    ),
    description = HtmlContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."),
    addressSegments = listOf("29 Bachelors Walk", "Dublin 1"),
    images = listOf(
        RemoteResource(
            "http://test-url#endofurl"
        ),
    ),
    location = PropertyLocation(
        city = City(
            id = "test-location",
            name = "Dublin",
            country = "Ireland"
        )
    ),
    isFeatured = false
)