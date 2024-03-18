package com.pedronsouza.data.dataSources

import com.pedronsouza.data.api.NetworkStatsApi
import com.pedronsouza.data.internal.ServicesFactory
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteNetworkMonitorDataSourceTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Test
    fun `Given a valid instance When try to track network stats Then should execute successfully`() {
        val api = object: NetworkStatsApi {
            override suspend fun trackNetworkStats(action: String, duration: Long) = Unit
        }

        val servicesFactory = object: ServicesFactory {
            override fun <T> getOrCreate(type: KClass<*>): T =
                api as T
        }

        val subject = RemoteNetworkMonitorDataSource(servicesFactory)

        runTest(testDispatcher) {
            subject.sendNetworkStats("", Long.MAX_VALUE)
        }
    }

    @Test
    fun `Given api is not working When try to track network stats Then should throws an Exception`() {
        val exception = UnsupportedOperationException()
        val api = object: NetworkStatsApi {
            override suspend fun trackNetworkStats(action: String, duration: Long) =
                throw exception
        }

        val servicesFactory = object: ServicesFactory {
            override fun <T> getOrCreate(type: KClass<*>): T =
                api as T
        }

        val subject = RemoteNetworkMonitorDataSource(servicesFactory)

        runTest(testDispatcher) {
            assertFailsWith<UnsupportedOperationException>() {
                subject.sendNetworkStats("", Long.MAX_VALUE)
            }
        }
    }

    @Test
    fun `Given servicesFactory is not working When try to track network stats Then should throws an Exception`() {
        val exception = UnsupportedOperationException()

        val servicesFactory = object: ServicesFactory {
            override fun <T> getOrCreate(type: KClass<*>): T =
                throw exception
        }

        val subject = RemoteNetworkMonitorDataSource(servicesFactory)

        runTest(testDispatcher) {
            assertFailsWith<UnsupportedOperationException>() {
                subject.sendNetworkStats("", Long.MAX_VALUE)
            }
        }
    }

}