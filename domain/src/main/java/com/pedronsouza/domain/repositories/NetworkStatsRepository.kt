package com.pedronsouza.domain.repositories

interface NetworkStatsRepository {
    suspend fun execute(requestIdentifier: String, totalElapseTime: Long)
}