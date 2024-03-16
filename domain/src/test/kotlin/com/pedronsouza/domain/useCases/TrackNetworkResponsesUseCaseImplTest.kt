package com.pedronsouza.domain.useCases

import com.pedronsouza.domain.repositories.NetworkStatsRepository
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TrackNetworkResponsesUseCaseImplTest {
    @Test
    fun `GIVEN that dependencies work as expected WHEN executing the useCase THEN it must return a Result with success`() {
        runTest {
            val networkStatsRepository = object: NetworkStatsRepository {
                override suspend fun sendNetworkStats(
                    requestIdentifier: String,
                    totalElapseTime: Long
                ) = Unit
            }
            val useCase = TrackNetworkResponsesUseCaseImpl(networkStatsRepository)
            val useCaseResult = useCase.execute("test-id", Long.MAX_VALUE)

            assertTrue(useCaseResult.isSuccess)
        }
    }

    @Test
    fun `GIVEN that currencyRepository does not work as expected WHEN executing the useCase THEN it must return a Result with the error`() {
        runTest {
            val networkStatsRepository = object: NetworkStatsRepository {
                override suspend fun sendNetworkStats(
                    requestIdentifier: String,
                    totalElapseTime: Long
                ) = throw Exception()
            }
            val useCase = TrackNetworkResponsesUseCaseImpl(networkStatsRepository)
            val useCaseResult = useCase.execute("test-id", Long.MAX_VALUE)

            assertTrue(useCaseResult.isFailure)
        }
    }
}