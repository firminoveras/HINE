package com.firmino.hinedigital.extensions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.firmino.hinedigital.R


@Composable
fun BallsAnim(modifier: Modifier = Modifier) {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val size = ((displayMetrics.heightPixels.toFloat() / 3) / displayMetrics.density)

    val scale1 = remember { Animatable(1.6f) }
    val scale2 = remember { Animatable(1.7f) }
    val scale3 = remember { Animatable(1.3f) }

    LaunchedEffect(true) {
        scale1.animateTo(
            targetValue = 2f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 10000, easing = EaseInOutSine
                )
            )
        )
    }

    LaunchedEffect(true) {
        scale2.animateTo(
            targetValue = 2f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 17500, easing = EaseInOutSine
                )
            )
        )
    }

    LaunchedEffect(true) {
        scale3.animateTo(
            targetValue = 2f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 22500, easing = EaseInOutSine
                )
            )
        )
    }

    Box(modifier
        .alpha(.8f)
        .fillMaxWidth()) {
        Image(
            painterResource(id = R.drawable.bg_circle_filled),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(size.dp)
                .offset { IntOffset(x = (size / (3 * scale1.value)).dp.toPx().toInt(), y = -(size / 2).dp.toPx().toInt()) }
                .scale(scale1.value),
        )

        Image(
            painterResource(id = R.drawable.bg_circle_notfilled),
            null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(size.dp)
                .offset { IntOffset(x = (size / (3 * scale2.value)).dp.toPx().toInt(), y = -(size / 1.7f).dp.toPx().toInt()) }
                .scale(scale2.value),
        )

        Image(
            painterResource(id = R.drawable.bg_circle_notfilled),
            null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(size.dp)
                .offset { IntOffset(x = -(size / (9 * scale3.value)).dp.toPx().toInt(), y = -(size / 1.5f).dp.toPx().toInt()) }
                .scale(scale3.value),
        )

        Image(
            painterResource(id = R.drawable.ic_logo_brand),
            null,
            contentScale = ContentScale.FillHeight,
            alignment = Alignment.TopEnd,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopEnd)
                .size((size / 1.4f).dp)
                .padding(16.dp)
        )
    }
}