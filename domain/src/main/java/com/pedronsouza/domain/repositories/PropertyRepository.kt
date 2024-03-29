package com.pedronsouza.domain.repositories

import com.pedronsouza.domain.models.Property

interface PropertyRepository {
    suspend fun fetch(): List<Property>
}