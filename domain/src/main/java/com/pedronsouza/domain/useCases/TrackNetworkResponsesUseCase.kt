package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.repositories.NetworkStatsRepository

interface TrackNetworkResponsesUseCase {
    suspend fun execute(requestIdentifier: String, totalElapseTime: Long): Result<Unit>
}

internal class TrackNetworkResponsesUseCaseImpl(
    private val networkStatsRepository: NetworkStatsRepository
) : TrackNetworkResponsesUseCase {
    override suspend fun execute(requestIdentifier: String, totalElapseTime: Long): Result<Unit> =
        runCatching {
            networkStatsRepository.sendNetworkStats(requestIdentifier, totalElapseTime)
        }
}