package com.pedronsouza.data.internal

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pedronsouza.data.api.PropertyApi
import kotlin.reflect.KClass
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

internal class ServicesFactory {
    private lateinit var propertyApi: PropertyApi
    private val retrofitInstance: Retrofit

    init {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
            )
            .build()

        retrofitInstance = Retrofit.Builder()
            .baseUrl("https://127.0.0.1")
            .client(okHttpClient)
            .addCallAdapterFactory(SuspendResultCallAdapterFactory())
            .addConverterFactory(DataJsonConfig.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    fun getOrCreate(type: KClass<*>) =
        when (type) {
            PropertyApi::class -> {
                if (!::propertyApi.isInitialized) {
                    propertyApi = retrofitInstance.create()
                }

                propertyApi
            }

            else -> throw IllegalArgumentException("No API found for type $type")
        }
}