package com.firmino.hinedigital.view.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.ContentText
import com.firmino.hinedigital.extensions.Title
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import kotlinx.coroutines.launch

@Composable
fun TextContentScreen(
    navController: NavController,
    textItems: List<Title>,
    title: String,
    subtitle: String,
    info: String,

    ) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val corroutineScope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val listState = rememberLazyListState()
        val mapScrollState = rememberScrollState()

        Box(Modifier.padding(innerPadding).fillMaxSize().background(Brush.linearGradient(listOf(ColorGender, ColorGenderDark)))) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(drawerContainerColor = Color.White, drawerContentColor = Color.White) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp).verticalScroll(mapScrollState)) {
                            Spacer(Modifier.height(12.dp))
                            Text("Sumário", color = ColorGenderDarker, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(12.dp))
                            textItems.forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    icon = { Icon(ImageVector.vectorResource(id = item.icon ?: R.drawable.ic_info), contentDescription = null) },
                                    label = { Text(item.smallTitle) },
                                    selected = listState.firstVisibleItemIndex == index,
                                    onClick = {
                                        corroutineScope.launch { listState.animateScrollToItem(index = index) }
                                        corroutineScope.launch { drawerState.close() }
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = ColorGenderDark,
                                        selectedTextColor = Color.White,
                                        selectedIconColor = Color.White,
                                    )
                                )
                            }
                        }
                    }
                }
            ) {
                Column {
                    GuideHeader(
                        title = title,
                        subtitle = subtitle,
                        info = info,
                        onBackButtonClick = { navController.popBackStack() },
                        content = {
                            IconButton(
                                onClick = { corroutineScope.launch { drawerState.open() } },
                                colors = IconButtonDefaults.iconButtonColors().copy(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White
                                ),
                                content = { Icon(ImageVector.vectorResource(id = R.drawable.ic_map), contentDescription = null) },
                            )
                        }
                    )
                    LazyColumn(state = listState) {
                        items(items = textItems) {
                            ContentText(content = it)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun GuideHeader(
    title: String,
    subtitle: String,
    info: String,
    onBackButtonClick: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val animScale = remember { Animatable(1.8f) }
    val animRotation = remember { Animatable(-150f) }
    LaunchedEffect(true) {
        animScale.animateTo(
            targetValue = 2.6f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 18000, easing = EaseInOutSine
                )
            )
        )
    }

    LaunchedEffect(true) {
        animRotation.animateTo(
            targetValue = 120f, animationSpec = infiniteRepeatable(
                repeatMode = RepeatMode.Reverse, animation = tween(
                    durationMillis = 43000, easing = EaseInOutSine
                )
            )
        )
    }

    Box(Modifier.shadow(elevation = 4.dp).background(color = ColorGenderDark).fillMaxWidth().height(220.dp)) {
        Image(
            ImageVector.vectorResource(R.drawable.ic_brain),
            contentDescription = null,
            modifier = Modifier
                .height(240.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
                .alpha(0.04f)
                .scale(animScale.value)
                .rotate(animRotation.value)
        )
        Button(
            modifier = Modifier.padding(top = 56.dp),
            onClick = { onBackButtonClick() },
            shape = ShapeDefaults.Large.copy(
                topStart = CornerSize(0.dp),
                bottomStart = CornerSize(0.dp),
                topEnd = CornerSize(120.dp),
                bottomEnd = CornerSize(120.dp),
            ),
            colors = ButtonDefaults.textButtonColors(
                containerColor = ColorGenderDarker,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(start = 8.dp, top = 6.dp, bottom = 6.dp, end = 12.dp),
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Voltar",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight(800),
                fontSize = 16.sp
            )
        }
        Text(
            modifier = Modifier.align(Alignment.TopEnd).padding(horizontal = 12.dp, vertical = 56.dp),
            text = stringResource(R.string.subtitle_hine),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight(800),
            textAlign = TextAlign.End,
            color = Color.White
        )
        Column(modifier = Modifier.padding(8.dp).align(Alignment.BottomEnd), content = content)
        Column(modifier = Modifier.padding(8.dp).align(Alignment.BottomStart)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = info,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}