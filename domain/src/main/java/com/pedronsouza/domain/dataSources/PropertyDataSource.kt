package com.pedronsouza.domain.dataSources

import com.pedronsouza.domain.models.Property

interface PropertyDataSource {
    suspend fun fetch(): List<Property>
}