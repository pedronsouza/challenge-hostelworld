package com.pedronsouza.data.internal

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pedronsouza.data.api.CurrencyApi
import com.pedronsouza.data.api.NetworkStatsApi
import com.pedronsouza.data.api.PropertyApi
import com.pedronsouza.data.api.interceptors.NetworkStatMonitorInterceptor
import kotlin.reflect.KClass
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

interface ServicesFactory {
    fun <T>getOrCreate(type: KClass<*>): T
}
internal class ServicesFactoryImpl : ServicesFactory {
    private lateinit var propertyApi: PropertyApi
    private lateinit var currencyApi: CurrencyApi
    private lateinit var networkStatsApi: NetworkStatsApi

    private val retrofitInstance: Retrofit by lazy {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
            )
            .addInterceptor(NetworkStatMonitorInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/PedroTrabulo-Hostelworld/")
            .client(okHttpClient)
            .addCallAdapterFactory(SuspendResultCallAdapterFactory())
            .addConverterFactory(DataJsonConfig.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T>getOrCreate(type: KClass<*>): T =
        when (type) {
            PropertyApi::class -> {
                if (!::propertyApi.isInitialized) {
                    propertyApi = retrofitInstance.create()
                }

                propertyApi
            }

            CurrencyApi::class -> {
                if (!::currencyApi.isInitialized) {
                    currencyApi = retrofitInstance.create()
                }

                currencyApi
            }

            NetworkStatsApi::class -> {
                if (!::networkStatsApi.isInitialized) {
                    networkStatsApi = retrofitInstance.create()
                }

                networkStatsApi
            }

            else -> throw IllegalArgumentException("No API found for type $type")
        }.run {
            this as T
        }
}