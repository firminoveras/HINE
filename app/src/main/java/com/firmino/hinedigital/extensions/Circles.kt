package com.firmino.hinedigital.extensions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.firmino.hinedigital.R


@Composable
fun BallsAnim() {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels.toFloat() / displayMetrics.density

    val scale1 = remember { Animatable(1f) }
    val scale2 = remember { Animatable(1f) }
    val scale3 = remember { Animatable(1f) }

    LaunchedEffect(true) {
        scale1.animateTo(
            targetValue = 1.8f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 32000, easing = EaseInOutSine
                )
            )
        )
    }
    LaunchedEffect(true) {
        scale2.animateTo(
            targetValue = 1.6f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 35000, easing = EaseInOutSine
                )
            )
        )
    }
    LaunchedEffect(true) {
        scale3.animateTo(
            targetValue = 1.4f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 36500, easing = EaseInOutSine
                )
            )
        )
    }
    Box(Modifier.alpha(.8f)) {
        Image(
            painterResource(id = R.drawable.bg_circle_filled),
            contentDescription = null,
            Modifier.fillMaxSize().scale(scale1.value).offset(x = (screenWidthDp / 3.5f).dp).padding(),
            Alignment.TopEnd
        )
        Image(
            painterResource(id = R.drawable.bg_circle_notfilled),
            null,
            Modifier.fillMaxSize().scale(scale2.value).offset(x = 32.dp + (screenWidthDp / 3.5f).dp, y = 62.dp),
            Alignment.TopEnd
        )
        Image(
            painterResource(id = R.drawable.bg_circle_notfilled),
            null,
            Modifier.fillMaxSize().scale(scale3.value).offset(x = 120.dp + (screenWidthDp / 3.5f).dp, y = 52.dp),
            Alignment.TopEnd
        )
    }
}

