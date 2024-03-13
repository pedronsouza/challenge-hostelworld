/*
* source from https://github.com/yujinyan/retrofit-suspend-result-adapter/blob/master/src/main/kotlin/me/yujinyan/retrofit/SuspendResultCallAdapterFactory.kt
* */
package com.pedronsouza.data.internal

import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.HttpURLConnection

internal class SuspendResultCallAdapterFactory(
    private val failureHandler: FailureHandler? = null
) : CallAdapter.Factory() {

    /**
     * [onFailure] will be called when [Result.isFailure]
     */
    fun interface FailureHandler {
        fun onFailure(throwable: Throwable)
    }

    private var hasConverterForResult: Boolean? = null
    private fun Retrofit.hasConverterForResultType(resultType: Type): Boolean {
        // If converter exists for any `Result<T>`,
        // user registered custom converter for `Result` type.
        // No need to check again.
        return if (hasConverterForResult == true) {
            true
        } else {
            runCatching {
                nextResponseBodyConverter<Result<*>>(
                    null,
                    resultType,
                    arrayOf()
                )
            }.isSuccess.also { hasConverterForResult = it }
        }
    }

    /**
     * Represents Type `Call<T>`, where `T` is passed in [dataType]
     */
    private class CallDataType(
        private val dataType: Type
    ) : ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> = arrayOf(dataType)
        override fun getRawType(): Type = Call::class.java
        override fun getOwnerType(): Type? = null
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // suspend function is represented by `Call<Result<T>>`
        if (getRawType(returnType) != Call::class.java || returnType !is ParameterizedType) {
            return null
        }

        // Result<T>
        val resultType: Type = getParameterUpperBound(0, returnType)
        if (getRawType(resultType) != Result::class.java ||
            resultType !is ParameterizedType
        ) {
            return null
        }

        val dataType = getParameterUpperBound(0, resultType)

        val delegateType = if (retrofit.hasConverterForResultType(resultType)) {
            returnType
        } else {
            CallDataType(dataType)
        }

        val delegate: CallAdapter<*, *> = retrofit
            .nextCallAdapter(this, delegateType, annotations)

        return CatchingCallAdapter(delegate, failureHandler)
    }

    private class CatchingCallAdapter(
        private val delegate: CallAdapter<*, *>,
        private val failureHandler: FailureHandler?
    ) : CallAdapter<Any, Call<Result<*>>> {
        override fun responseType(): Type = delegate.responseType()
        override fun adapt(call: Call<Any>): Call<Result<*>> = CatchingCall(call, failureHandler)
    }

    private class CatchingCall(
        private val delegate: Call<Any>,
        private val failureHandler: FailureHandler?
    ) : Call<Result<*>> {

        override fun enqueue(callback: Callback<Result<*>>) = delegate.enqueue(object :
                Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        callback.onResponse(this@CatchingCall, Response.success(Result.success(body)))
                    } else {
                        val throwable = HttpException(response)
                        failureHandler?.onFailure(throwable)

                        callback.onResponse(
                            this@CatchingCall,
                            Response.success(Result.failure<HttpException>(throwable))
                        )
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    failureHandler?.onFailure(t)
                    callback.onResponse(
                        this@CatchingCall,
                        Response.success(Result.failure<Any>(t))
                    )
                }
            })

        override fun clone(): Call<Result<*>> = CatchingCall(delegate, failureHandler)
        override fun execute(): Response<Result<*>> =
            throw UnsupportedOperationException("Suspend function should not be blocking.")

        override fun isExecuted(): Boolean = delegate.isExecuted
        override fun cancel(): Unit = delegate.cancel()
        override fun isCanceled(): Boolean = delegate.isCanceled
        override fun request(): Request = delegate.request()
        override fun timeout(): Timeout = delegate.timeout()
    }
}
