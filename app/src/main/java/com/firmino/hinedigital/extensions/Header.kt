package com.firmino.hinedigital.extensions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmino.hinedigital.R
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.mplus

@Composable
fun Header(modifier: Modifier = Modifier, onBackPressed: () -> Unit = {}, backButtonText: String = "Voltar", title: String = "", subtitle1: String = "", subtitle2: String = "") {
    val scale1 = remember { Animatable(1.2f) }
    val scale2 = remember { Animatable(1f) }
    val scale3 = remember { Animatable(1f) }

    LaunchedEffect(true) {
        scale1.animateTo(
            targetValue = 2f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 32000, easing = EaseInOutSine
                )
            )
        )
    }

    LaunchedEffect(true) {
        scale2.animateTo(
            targetValue = 1.9f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 43500, easing = EaseInOutSine
                )
            )
        )
    }

    LaunchedEffect(true) {
        scale2.animateTo(
            targetValue = 1.92f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 51500, easing = EaseInOutSine
                )
            )
        )
    }

    Column(
        modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Image(
                painterResource(id = R.drawable.bg_circle_filled), contentDescription = null,
                Modifier
                    .fillMaxSize()
                    .alpha(.8f)
                    .scale(scale1.value)
                    .offset(y = (-60).dp, x = (-90).dp), Alignment.BottomCenter
            )
            Image(
                painterResource(id = R.drawable.bg_circle_notfilled), contentDescription = null,
                Modifier
                    .fillMaxSize()
                    .alpha(.8f)
                    .scale(scale2.value)
                    .offset(y = (-120).dp, x = (-18).dp), Alignment.BottomCenter
            )
            Image(
                painterResource(id = R.drawable.bg_circle_notfilled), contentDescription = null,
                Modifier
                    .fillMaxSize()
                    .alpha(.8f)
                    .scale(scale3.value)
                    .offset(y = (-110).dp, x = 70.dp), Alignment.BottomCenter
            )
            Row(
                modifier
                    .height(160.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier.weight(1f)) {
                    Button(
                        onClick = { onBackPressed() },
                        shape = ShapeDefaults.Large.copy(
                            topStart = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp),
                            topEnd = CornerSize(120.dp),
                            bottomEnd = CornerSize(120.dp),
                        ),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = ColorGenderDark,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(start = 8.dp, top = 6.dp, bottom = 6.dp, end = 12.dp),
                    ) {
                        Icon(Icons.Rounded.Menu, contentDescription = null, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = backButtonText,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight(800),
                            fontSize = 16.sp
                        )
                    }
                }
                Column(
                    modifier
                        .weight(1f)
                        .padding(end = 16.dp), horizontalAlignment = Alignment.End
                ) {
                    Text(text = subtitle1, color = Color.White, textAlign = TextAlign.End, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = subtitle2, color = Color.White, textAlign = TextAlign.End, fontSize = 12.sp)
                }
            }
            if (title.isNotBlank()) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    textAlign = TextAlign.Center, text = title.uppercase(), style = MaterialTheme.typography.headlineSmall, fontFamily = mplus, color = Color.White, fontWeight = FontWeight(800)
                )
            }
        }
    }


}
