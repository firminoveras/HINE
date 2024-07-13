package com.firmino.hinedigital.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.Header
import com.firmino.hinedigital.extensions.Title
import com.firmino.hinedigital.extensions.guideItems
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.view.theme.ThemeGender
import com.firmino.hinedigital.view.theme.getColorTheme
import com.firmino.hinedigital.view.theme.toggleTheme
import kotlinx.coroutines.launch

class GuideActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HINEDigitalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var theme by remember { mutableStateOf(getColorTheme(this@GuideActivity) == ThemeGender.FEMALE) }
                    var scale by remember { mutableFloatStateOf(1f) }
                    val pagerState = rememberPagerState(pageCount = { guideItems.size })
                    val corroutineScope = rememberCoroutineScope()

                    key(theme) {
                        Column(
                            Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(Brush.linearGradient(listOf(ColorGender, ColorGenderDark))), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Header(title = stringResource(R.string.hine_guide), subtitle1 = stringResource(R.string.hine_extend), subtitle2 = stringResource(R.string.hine_date), onBackPressed = {
                                startActivity(Intent(this@GuideActivity, MenuActivity::class.java))
                                finish()
                            })
                            Box(Modifier.fillMaxSize()) {
                                HorizontalPager(modifier = Modifier
                                    .padding(bottom = 120.dp)
                                    .fillMaxSize(), state = pagerState) { page -> ContentText(content = guideItems[page], scale = scale) }
                                PageNavigation(
                                    modifier = Modifier.align(Alignment.BottomCenter),
                                    state = pagerState,
                                    onThemeChange = { theme = toggleTheme(this@GuideActivity) },
                                    onScalePlus = { scale += 0.1f },
                                    onScaleMinus = { scale -= 0.1f }) { corroutineScope.launch { pagerState.animateScrollToPage(it) } }
                            }
                        }
                    }

                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PageNavigation(modifier: Modifier = Modifier, state: PagerState, onThemeChange: () -> Unit = {}, onScalePlus: () -> Unit = {}, onScaleMinus: () -> Unit = {}, onNavigate: (newPage: Int) -> Unit) {
        Column(modifier.height(120.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                    onClick = { onThemeChange() }) {
                    Icon(ImageVector.vectorResource(id = R.drawable.ic_theme), contentDescription = null, tint = ColorGenderDark)
                }
                IconButton(colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                    onClick = { onScaleMinus() }) {
                    Icon(ImageVector.vectorResource(id = R.drawable.ic_font_minus), contentDescription = null, tint = ColorGenderDark)
                }
                IconButton(colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                    onClick = { onScalePlus() }) {
                    Icon(ImageVector.vectorResource(id = R.drawable.ic_font_plus), contentDescription = null, tint = ColorGenderDark)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Surface(
                    modifier = Modifier.alpha(if (state.currentPage == 0) 0f else 1f),
                    onClick = { onNavigate(state.currentPage - 1) },
                    shape = CircleShape,
                    color = Color.White,
                    contentColor = ColorGenderDark,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = null, modifier = Modifier
                            .padding(8.dp)
                            .size(38.dp)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    repeat(state.pageCount) { page ->
                        val color = if (page == state.currentPage) ColorGender else ColorGenderDarker
                        Spacer(
                            Modifier
                                .size(if (page == state.currentPage) 12.dp else 6.dp)
                                .background(color, shape = CircleShape)
                        )
                    }
                }
                Surface(
                    modifier = Modifier.alpha(if (state.currentPage == state.pageCount - 1) 0f else 1f),
                    onClick = { onNavigate(state.currentPage + 1) },
                    shape = CircleShape,
                    color = Color.White,
                    contentColor = ColorGenderDark,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, modifier = Modifier
                            .padding(8.dp)
                            .size(38.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun ContentText(modifier: Modifier = Modifier, content: Title, scale: Float) {
        Box(modifier) {
            LazyColumn(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .scrollable(rememberScrollState(), Orientation.Vertical)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Spacer(Modifier.height(12.dp)) }
                item {
                    Text(
                        modifier = Modifier
                            .background(color = ColorGender, shape = RoundedCornerShape(32.dp))
                            .padding(8.dp), text = content.title, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold
                    )
                }
                content.items.forEach {
                    item {
                        when (it.type) {
                            com.firmino.hinedigital.extensions.ParagraphType.PARAGRAPH -> GuideParagraph(text = stringResource(id = it.id), scale = scale)
                            com.firmino.hinedigital.extensions.ParagraphType.TOPIC -> GuideTopic(text = stringResource(id = it.id), scale = scale)
                        }
                    }
                }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }


    @Composable
    private fun GuideTopic(text: String, scale: Float = 1f) {
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = " ○ ", textAlign = TextAlign.Justify, fontSize = (scale * 14).sp, lineHeight = (scale * 15).sp, color = ColorGenderDark)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, textAlign = TextAlign.Justify, fontSize = (scale * 14).sp, lineHeight = (scale * 15).sp, color = ColorGenderDark)
        }
    }

    @Composable
    private fun GuideParagraph(text: String, scale: Float = 1f) {
        Text(text = text, textAlign = TextAlign.Justify, fontSize = (scale * 16).sp, lineHeight = (scale * 17).sp, color = ColorGenderDark)
    }

}