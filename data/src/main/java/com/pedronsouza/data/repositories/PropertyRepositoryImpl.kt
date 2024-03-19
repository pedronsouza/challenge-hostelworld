package com.pedronsouza.data.repositories

import com.pedronsouza.domain.dataSources.PropertyDataSource
import com.pedronsouza.domain.repositories.PropertyRepository
import com.pedronsouza.domain.models.Property

internal class PropertyRepositoryImpl(
    private val dataSource: PropertyDataSource
) : PropertyRepository {
    override suspend fun fetch(): List<Property> =
        dataSource.fetch()

    override suspend fun getById(id: String): Property =
        dataSource.fetch().run {
            first { it.id == id }
        }
}