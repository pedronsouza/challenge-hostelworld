package com.pedronsouza.data.repositories

import com.pedronsouza.data.api.NetworkStatsApi
import com.pedronsouza.data.internal.ServicesFactory
import com.pedronsouza.domain.repositories.NetworkStatsRepository

internal class NetworkStatsRepositoryImpl(
    private val servicesFactory: ServicesFactory
) : NetworkStatsRepository {
    private val api: NetworkStatsApi by lazy { servicesFactory.getOrCreate(NetworkStatsApi::class) }

    override suspend fun execute(requestIdentifier: String, totalElapseTime: Long) {
        api.trackNetworkStats(requestIdentifier, totalElapseTime)
    }
}