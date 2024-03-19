package com.pedronsouza.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import com.pedronsouza.shared.components.brushes.shimmerBrush

@Composable
fun LoadingView() {
    LazyColumn(userScrollEnabled = false) {
        items(10) {
            ConstraintLayout(
                modifier = Modifier.padding(LocalDimensions.current.defaultScreenPadding)
            ) {
                val (image, content, rating) = createRefs()

                val imageConstraint: ConstrainScope.() -> Unit = {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalDimensions.current.propertyShowroomImageSize)
                        .constrainAs(image, imageConstraint)
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = true
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(LocalDimensions.current.innerTextContentPropertyCardPadding)
                        .constrainAs(content) {
                            top.linkTo(image.bottom)
                            start.linkTo(image.start)
                        }
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = true
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .height(64.dp)
                        .padding(
                            LocalDimensions.current.innerTextContentPropertyCardPadding
                        )
                        .constrainAs(rating) {
                            top.linkTo(content.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = true
                            )
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun previewLoadingView() {
    LoadingView()
}