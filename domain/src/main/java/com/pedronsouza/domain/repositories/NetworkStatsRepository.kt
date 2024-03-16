package com.pedronsouza.domain.repositories

interface NetworkStatsRepository {
    suspend fun sendNetworkStats(requestIdentifier: String, totalElapseTime: Long)
}