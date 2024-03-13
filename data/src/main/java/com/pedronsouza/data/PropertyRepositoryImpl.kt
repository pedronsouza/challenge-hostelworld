package com.pedronsouza.data

import com.pedronsouza.domain.repositories.PropertyRepository
import com.pedronsouza.domain.models.Property

internal class PropertyRepositoryImpl : PropertyRepository {
    override fun fetch(): Result<List<Property>> {
        TODO("Not yet implemented")
    }
}