package com.pedronsouza.data.repositories

import com.pedronsouza.data.dataSources.NetworkMonitorDataSource
import com.pedronsouza.domain.repositories.NetworkStatsRepository

internal class NetworkStatsRepositoryImpl(
    private val dataSource: NetworkMonitorDataSource
) : NetworkStatsRepository {

    override suspend fun sendNetworkStats(requestIdentifier: String, totalElapseTime: Long) {
        dataSource.sendNetworkStats(requestIdentifier, totalElapseTime)
    }
}