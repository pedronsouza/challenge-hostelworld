package com.pedronsouza.shared.mappers

import com.pedronsouza.domain.ContentParser
import com.pedronsouza.domain.dataSources.LocalCurrencyDataSource
import com.pedronsouza.domain.mappers.ObjectMapper
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.values.HtmlContent
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.components.models.RatingCategory
import com.pedronsouza.shared.extensions.priceFormatted

interface PropertyListMapper : ObjectMapper<List<Property>, List<PropertyItem>>
internal class PropertyListMapperImpl(
    private val parser: ContentParser<HtmlContent, String>,
    private val currencyLocalCache: LocalCurrencyDataSource
) : PropertyListMapper {
    override fun transform(inputData: List<Property>) =
        inputData.map { item ->
            PropertyItem(
                id = item.name,
                name = item.name,
                lowestPriceByNight = item.lowestPriceByNight,
                displayPrice = item.priceFormatted(currencyLocalCache.getSelectedCurrency()),
                images = item.images.map { it.toString() },
                description = parser.parse(item.description),
                rating = mapOf(
                    RatingCategory.OVERALL to calculateRating(item.rating.overall),
                    RatingCategory.SECURITY to calculateRating(item.rating.security),
                    RatingCategory.FACILITIES to calculateRating(item.rating.facilities),
                    RatingCategory.AVERAGE to calculateRating(item.rating.average),
                    RatingCategory.CLEAN to calculateRating(item.rating.clean),
                    RatingCategory.STAFF to calculateRating(item.rating.staff),
                    RatingCategory.LOCATION to calculateRating(item.rating.location)
                ),
                address = item.addressSegments.joinToString { "," },
                location = "${item.location.city.name}, ${item.location.city.country}",
                isFeatured = item.isFeatured
            )
        }

    private fun calculateRating(rating: Int): Double =
        rating / 10.0
}