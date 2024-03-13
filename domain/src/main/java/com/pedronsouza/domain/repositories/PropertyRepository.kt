package com.pedronsouza.domain.repositories

import com.pedronsouza.domain.models.Property

interface PropertyRepository {
    fun fetch(): List<Property>
}