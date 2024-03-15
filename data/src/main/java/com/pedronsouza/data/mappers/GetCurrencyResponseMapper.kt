package com.pedronsouza.data.mappers

import com.pedronsouza.data.responses.GetCurrenciesResponse
import com.pedronsouza.domain.mappers.ObjectMapper
import com.pedronsouza.domain.models.Currency

interface GetCurrencyResponseMapper : ObjectMapper<GetCurrenciesResponse, List<Currency>>
internal class GetCurrencyResponseMapperImpl : GetCurrencyResponseMapper {
    override fun transform(inputData: GetCurrenciesResponse): List<Currency> =
        inputData.rates.map { (currencyCode, rate) ->
            Currency(
                currencyCode = currencyCode,
                rate = rate
            )
        }
}