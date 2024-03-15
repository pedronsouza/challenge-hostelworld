package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.repositories.PropertyRepository

interface LoadPropertiesUseCase {
    suspend fun execute(): Result<List<Property>>
}

class LoadPropertiesUseCaseImpl(private val propertyRepository: PropertyRepository) : LoadPropertiesUseCase {
    override suspend fun execute() =
        runCatching {
            propertyRepository.fetch()
        }
}