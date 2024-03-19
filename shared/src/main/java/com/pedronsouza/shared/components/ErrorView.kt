package com.pedronsouza.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pedronsouza.shared.R

@Composable
fun ErrorView(error: Throwable, onRetryClicked: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.current.white)
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Outlined.Warning),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = LocalColors.current.mediumGray
        )


        Text(
            text = stringResource(
                id = R.string.something_went_wrong
            ) + error.message?.let { ": $it" }.orEmpty()
        )

        Button(
            onClick = onRetryClicked
        ) {
            Text(text = "Retry")
        }
    }
}

@Preview
@Composable
fun previewErrorView() {
    ErrorView(
        error = IllegalArgumentException("Error message here"),
        onRetryClicked = { }
    )
}