package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.repositories.PropertyRepository

class LoadPropertiesUseCase(private val propertyRepository: PropertyRepository) {
    suspend fun execute() =
        runCatching {
            propertyRepository.fetch()
        }
}