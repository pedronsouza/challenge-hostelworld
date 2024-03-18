package com.pedronsouza.data.repositories

import com.pedronsouza.data.dataSources.NetworkMonitorDataSource
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkStatsRepositoryImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Test
    fun `Given a valid instance When track network is invoked Then it should runs successfully`() {
        val dataSource = object: NetworkMonitorDataSource {
            override suspend fun sendNetworkStats(
                requestIdentifier: String,
                totalElapseTime: Long
            ) = Unit
        }

        val subject = NetworkStatsRepositoryImpl(dataSource)

        runTest {
            subject.sendNetworkStats("", Long.MAX_VALUE)
        }
    }

    @Test
    fun `Given a valid instance When track network fails when invoked Then it should throw an error`() {
        val exception = UnsupportedOperationException()

        val dataSource = object: NetworkMonitorDataSource {
            override suspend fun sendNetworkStats(
                requestIdentifier: String,
                totalElapseTime: Long
            ) {
                throw exception
            }
        }

        val subject = NetworkStatsRepositoryImpl(dataSource)

        runTest {
            assertFailsWith<UnsupportedOperationException> {
                subject.sendNetworkStats("", Long.MAX_VALUE)
            }
        }
    }
}