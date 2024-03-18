package com.pedronsouza.data.dataSources

import com.pedronsouza.data.api.NetworkStatsApi
import com.pedronsouza.data.internal.ServicesFactory

interface NetworkMonitorDataSource {
    suspend fun sendNetworkStats(requestIdentifier: String, totalElapseTime: Long)
}

internal class RemoteNetworkMonitorDataSource(
    private val servicesFactory: ServicesFactory
) : NetworkMonitorDataSource {

    private val api: NetworkStatsApi by lazy { servicesFactory.getOrCreate(NetworkStatsApi::class) }

    override suspend fun sendNetworkStats(requestIdentifier: String, totalElapseTime: Long) =
        api.trackNetworkStats(requestIdentifier, totalElapseTime)
}