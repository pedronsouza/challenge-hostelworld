package com.pedronsouza.data.dataSources

import com.pedronsouza.data.api.PropertyApi
import com.pedronsouza.data.internal.ServicesFactory
import com.pedronsouza.data.mappers.PropertyMapper
import com.pedronsouza.domain.dataSources.PropertyDataSource
import com.pedronsouza.domain.models.Property

internal class RemotePropertyDataSource(
    private val servicesFactory: ServicesFactory,
    private val propertyObjectMapper: PropertyMapper
) : PropertyDataSource {
    private val propertyApi: PropertyApi by lazy { servicesFactory.getOrCreate(PropertyApi::class) }

    override suspend fun fetch(): List<Property> =
        propertyObjectMapper.transform(
            inputData = propertyApi.getProperties()
        )
}