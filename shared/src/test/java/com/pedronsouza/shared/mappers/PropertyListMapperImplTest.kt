package com.pedronsouza.shared.mappers

import com.pedronsouza.domain.ContentParser
import com.pedronsouza.domain.dataSources.LocalCurrencyDataSource
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.Currency
import com.pedronsouza.domain.values.AppCurrency
import com.pedronsouza.domain.values.HtmlContent
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.components.models.RatingCategory
import com.pedronsouza.shared.extensions.priceFormatted
import com.pedronsouza.shared_test.fakes.FakeLocalCurrencyDataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PropertyListMapperImplTest {
    private val parser = object: ContentParser<HtmlContent, String> {
        override fun parse(value: HtmlContent): String =
            value.toString()
    }

    @Test
    fun `Given a valid instance of the mapper When transforming data Then should return the expected PropertyItem`() {
        val subject = PropertyListMapperImpl(
            parser = parser,
            currencyLocalCache = FakeLocalCurrencyDataSource
        )

        assertEquals(
            listOf(
                PropertyItem(
                    id = FakeProperty.id,
                    name = FakeProperty.name,
                    lowestPriceByNight = FakeProperty.lowestPriceByNight,
                    displayPrice = FakeProperty.priceFormatted(AppCurrency("EUR")),
                    images = FakeProperty.images.map { it.toString() },
                    description = parser.parse(FakeProperty.description),
                    rating = mapOf(
                        RatingCategory.OVERALL to calculateRating(FakeProperty.rating.overall),
                        RatingCategory.SECURITY to calculateRating(FakeProperty.rating.security),
                        RatingCategory.FACILITIES to calculateRating(FakeProperty.rating.facilities),
                        RatingCategory.AVERAGE to calculateRating(FakeProperty.rating.average),
                        RatingCategory.CLEAN to calculateRating(FakeProperty.rating.clean),
                        RatingCategory.STAFF to calculateRating(FakeProperty.rating.staff),
                        RatingCategory.LOCATION to calculateRating(FakeProperty.rating.location)
                    ),
                    address = FakeProperty.addressSegments.joinToString { "," },
                    location = "${FakeProperty.location.city.name}, ${FakeProperty.location.city.country}",
                    isFeatured = FakeProperty.isFeatured
                )
            ),
            subject.transform(listOf(FakeProperty))
        )
    }

    @Test
    fun `Given parser is failing When transforming data Then should throw the expected exception`() {
        val expectedException = UnsupportedOperationException()
        val subject = PropertyListMapperImpl(
            parser = object: ContentParser<HtmlContent, String> {
                override fun parse(value: HtmlContent): String {
                    throw expectedException
                }
            },
            currencyLocalCache = FakeLocalCurrencyDataSource
        )

        assertFailsWith<UnsupportedOperationException> {
            subject.transform(listOf(FakeProperty))
        }
    }

    @Test
    fun `Given localCache is failing When transforming data Then should throw the expected exception`() {
        val expectedException = UnsupportedOperationException()
        val subject = PropertyListMapperImpl(
            parser = parser,
            currencyLocalCache = object: LocalCurrencyDataSource {
                override fun isWarmed(): Boolean { TODO("Not yet implemented") }
                override fun addToCache(list: List<Currency>) { TODO("Not yet implemented") }
                override suspend fun getCurrencies(): List<Currency> { TODO("Not yet implemented") }
                override fun getSelectedCurrency(): AppCurrency { throw  expectedException }
                override fun setSelectedCurrency(newCurrency: AppCurrency) { }
            }
        )

        assertFailsWith<UnsupportedOperationException> {
            subject.transform(listOf(FakeProperty))
        }
    }

    private fun calculateRating(value: Int) = value / 10.0
}