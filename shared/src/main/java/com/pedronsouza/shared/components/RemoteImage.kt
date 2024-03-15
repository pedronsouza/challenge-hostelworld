package com.pedronsouza.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pedronsouza.shared.components.brushes.shimmerBrush

@Composable
fun RemoteImage(url: String, modifier: Modifier = Modifier) {
    val showShimmer = remember { mutableStateOf(true) }
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.background(
            shimmerBrush(
                targetValue = 1300f,
                showShimmer = showShimmer.value
            )
        ),

        onSuccess = { showShimmer.value = false }
    )
}

@Preview
@Composable
fun previewRemoteImage() {
    RemoteImage(url = "test-url", modifier = Modifier.height(200.dp))
}
