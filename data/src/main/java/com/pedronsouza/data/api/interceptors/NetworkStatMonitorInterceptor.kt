package com.pedronsouza.data.api.interceptors

import com.pedronsouza.domain.useCases.TrackNetworkResponsesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

internal class NetworkStatMonitorInterceptor : Interceptor, KoinComponent {
    private val logTag = this.javaClass.name

    private val trackNetworkResponsesUseCase by inject<TrackNetworkResponsesUseCase>()
    private val instanceScope: CoroutineScope get() = CoroutineScope(Dispatchers.IO)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toUrl().toString()
        val response = chain.proceed(request)

        RequestIdentifier.entries.onEach { requestIdentifier ->
            if (url.contains(requestIdentifier.reqIdentifier)) {
                val fullRequestDuration = response.receivedResponseAtMillis - response.sentRequestAtMillis
                instanceScope.launch {
                    trackNetworkResponsesUseCase.execute(
                        requestIdentifier.networkStatsName,
                        fullRequestDuration
                    ).onSuccess {
                        Timber.tag(logTag)
                            .d("Tracking new request\nRequest:$requestIdentifier\nTotal Time Elapsed: $fullRequestDuration")
                    }.onFailure { error ->
                        Timber.tag(logTag).e("Error in Tracking Request")
                        Timber.tag(logTag).e(error)
                    }
                }

                return@onEach
            }
        }

        return response
    }

    internal enum class RequestIdentifier(val networkStatsName: String, val reqIdentifier: String) {
        PROPERTY_LIST("load", "properties.json"),
        AVAILABLE_CURRENCIES("load-currencies", "rates.json")
    }
}