package com.pedronsouza.shared_test

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import coil.Coil
import coil.ComponentRegistry
import coil.ImageLoader
import coil.decode.DataSource
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CoilRule : TestWatcher() {
    override fun starting(description: Description) {
        Coil.setImageLoader(FakeImageLoader())
    }

    override fun finished(description: Description) {
        Coil.reset()
    }
}

internal class FakeImageLoader(
    override val components: ComponentRegistry = ComponentRegistry(),
    override val defaults: DefaultRequestOptions = DefaultRequestOptions(),
    override val diskCache: DiskCache? = null,
    override val memoryCache: MemoryCache? = null

) : ImageLoader {
    override fun enqueue(request: ImageRequest) = object: Disposable {
        override val isDisposed: Boolean = true
        override val job: Deferred<ImageResult> = CompletableDeferred(
            newResult(
                request = request,
                drawable = ColorDrawable(Color.BLACK)
            )
        )

        override fun dispose() = Unit
    }

    override fun shutdown() = Unit

    private fun newResult(request: ImageRequest, drawable: Drawable): SuccessResult {
        return SuccessResult(
            drawable = drawable,
            request = request,
            dataSource = DataSource.MEMORY_CACHE
        )
    }

    override suspend fun execute(request: ImageRequest): ImageResult =
        newResult(request, ColorDrawable(Color.BLACK))

    override fun newBuilder() = throw UnsupportedOperationException()

}

