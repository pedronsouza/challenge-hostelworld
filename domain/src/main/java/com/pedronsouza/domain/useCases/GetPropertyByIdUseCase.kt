package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.models.Property

interface GetPropertyByIdUseCase {
    suspend fun execute(propertyId: String): Result<Property>
}

internal class GetPropertyByIdUseCaseImpl(
    private val loadPropertiesUseCase: LoadPropertiesUseCase

) : GetPropertyByIdUseCase {
    override suspend fun execute(propertyId: String): Result<Property> =
        runCatching {
                loadPropertiesUseCase
                    .execute()
                    .getOrThrow()
                    .first { it.id == propertyId }
        }
}