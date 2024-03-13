package com.pedronsouza.data.api

import com.pedronsouza.data.internal.Constants
import com.pedronsouza.data.responses.GetPropertiesResponse
import retrofit2.http.GET
import retrofit2.http.Url


interface PropertyApi {
    @GET
    suspend fun getProperties(@Url url: String = Constants.GET_PROPERTIES_SERVICE): GetPropertiesResponse
}