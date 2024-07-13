package com.firmino.hinedigital.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.Header
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.view.theme.ThemeGender
import com.firmino.hinedigital.view.theme.getColorTheme
import com.firmino.hinedigital.view.theme.toggleTheme
import kotlinx.coroutines.launch

class InformationActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HINEDigitalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var theme by remember { mutableStateOf(getColorTheme(this) == ThemeGender.FEMALE) }
                    var scale by remember { mutableFloatStateOf(1f) }
                    val pagerState = rememberPagerState(pageCount = { 10 })
                    val corroutineScope = rememberCoroutineScope()

                    key(theme) {
                        Column(
                            Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(Brush.linearGradient(listOf(ColorGender, ColorGenderDark))), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Header(title = "INFORMAÇÕES", subtitle1 = stringResource(R.string.hine_extend), subtitle2 = stringResource(R.string.hine_date), onBackPressed = {
                                startActivity(Intent(this@InformationActivity, MenuActivity::class.java))
                                finish()
                            })
                            Box(Modifier.fillMaxSize()) {
                                HorizontalPager(
                                    modifier = Modifier
                                        .padding(bottom = 120.dp)
                                        .fillMaxSize(), state = pagerState
                                ) { page -> Content(page = page, scale = scale) }
                                PageNavigation(
                                    modifier = Modifier.align(Alignment.BottomCenter),
                                    state = pagerState,
                                    onThemeChange = { theme = toggleTheme(this@InformationActivity) },
                                    onScalePlus = { scale += 0.1f },
                                    onScaleMinus = { scale -= 0.1f }) { corroutineScope.launch { pagerState.animateScrollToPage(it) } }
                            }
                        }
                    }
                }
            }


        }
    }

    @Composable
    private fun Content(page: Int, scale: Float) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .scrollable(rememberScrollState(), Orientation.Vertical)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                item { Spacer(Modifier.height(12.dp)) }
                when (page) {
                    0 -> {
                        item { Image(painter = painterResource(id = R.drawable.bg_hine_folder), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillWidth) }
                    }

                    1 -> {
                        item { GuideTitle(text = "Informações Detalhadas") }
                        item { GuideParagraph("Paralisia Cerebral, PC ou Encefalopatia Crônica não Progressiva.s", scale) }
                        item { GuideSubtitle("Definição", scale) }
                        item { GuideParagraph("Desordem cerebral permanente não progressiva do sistema nervoso  central (SNC).", scale) }
                        item { GuideSubtitle("Ocorrência", scale) }
                        item { GuideParagraph("Durante o desenvolvimento do SNC", scale) }
                        item { GuideTopic("Período pré-natal;", scale) }
                        item { GuideTopic("Perinatal;", scale) }
                        item { GuideTopic("Pós-natal;", scale) }
                        item { GuideTopic("Comprometimento da área motora e postural;", scale) }
                        item { GuideTopic("É a maior causa de incapacidade física na infância;", scale) }
                        item { GuideTopic("Prevalência 2:1 por cada 1000 nascidos vivos.", scale) }
                    }
                    2 -> {
                        item { GuideTitle(text = "Informações Detalhadas") }
                        item { GuideSubtitle("Classificação", scale) }
                        item { GuideTopic("Espástica;", scale) }
                        item { GuideTopic("Discinética;", scale) }
                        item { GuideTopic("Atáxica;", scale) }
                        item { GuideTopic("Mista.", scale) }
                        item { GuideSubtitle("Complicações", scale) }
                        item { GuideTopic("Incapacidade Física;", scale) }
                        item { GuideTopic("Com Alterações Sensoriais;", scale) }
                        item { GuideTopic("Cognitivas;", scale) }
                        item { GuideTopic("Comunicação;", scale) }
                        item { GuideTopic("Comportamental;", scale) }
                        item { GuideTopic("Epilepsias.", scale) }
                    }

                    3 -> {
                        item { GuideTitle(text = "Diagnóstico") }
                        item { GuideSubtitle("Anamnese", scale) }
                        item { GuideTopic("Antecedentes gestacionais, perinatais e pós natais. ", scale) }
                        item { GuideSubtitle("Exame físico e neurológico", scale) }
                        item { GuideTopic("Identificar atraso no desenvolvimento neuropsicomotor;", scale) }
                        item { GuideTopic("Exame Neurológico Infantil De Hammersmith (HINE).", scale) }
                        item { GuideSubtitle("Exame de imagem", scale) }
                        item { GuideTopic("Ultrassonografia Transfontanela (U.S.T.F).", scale) }
                        item { GuideTopic("Tomografia Computadorizada De Crânio (T.C.C).", scale) }
                        item { GuideTopic("Ressonância Magnética Encéfalo (R.M.E).", scale) }
                    }

                    4 -> {
                        item { GuideTitle(text = "Neuroproteção") }
                        item { GuideParagraph("Medidas e ações preventivas de cuidados com Neurodesenvolvimento Encefálico", scale) }
                        item { GuideTopic("Pré-natal", scale) }
                        item { GuideTopic("Acompanhamento e assistência à gestação, mãe/feto.", scale,1) }
                        item { GuideTopic("Assistência ao RN em sala de parto por neonatologista.", scale,1) }
                        item { GuideTopic("Linha do cuidado Neonatal", scale) }
                        item { GuideTopic("1. Alojamento Conjunto;", scale,1) }
                        item { GuideTopic("2. Unidade De Cuidado Semi Intensivos;", scale, 1) }
                        item { GuideTopic("3. Unidade De Cuidado Intensivos (Manuseio Mínimo);", scale,1) }
                        item { GuideTopic("4. Unidade De Cuidados Canguru;", scale,1) }
                        item { GuideTopic("5. Puericultura e Follow Up Do RN Prematuro.", scale,1) }
                        item { GuideTopic("Triagens Neonatal;", scale) }
                        item { GuideTopic("Imunização Padrão;", scale) }
                        item { GuideTopic("Marcos do Desenvolvimento Neuropsicomotor.", scale) }
                    }

                    5 -> {
                        item { GuideTitle(text = "Reflexos Primitivos") }
                        item { GuideParagraph("Os reflexos primitivos presentes ao nascimento, devem ser substituídos ao longo dos primeiros meses por reflexos posturais. São reflexos automáticos e estereotipados a um determinado estímulo externo e interno. Demonstram integridade do SNC. Se persistem ao longo do tempo evidenciam disfunção neurológica.", scale) }
                    }

                    6 -> {
                        item { Image(painter = painterResource(id = R.drawable.bg_reflexes1), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillWidth) }
                    }

                    7 -> {
                        item { Image(painter = painterResource(id = R.drawable.bg_reflexes2), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillWidth) }
                    }

                    8 -> {
                        item { GuideSubtitle("Neuroplasticidade cerebral 1000 dias de vida", scale) }
                        item { GuideParagraph("É o processo contínuo de adaptação e reorganização do snc através de novas sinapses, em resposta aos estímulos do meio interno e externo, frente a novos aprendizados e ou injúrias encefálicas, que ocorrem entre nascimento até os 2 anos de idade.", scale) }
                        item { GuideSubtitle("Marcos Do Desenvolvimento Neuropsicomotor", scale) }
                        item { GuideParagraph("Conjunto de habilidades esperadas durante o crescimento e desenvolvimento da criança, nos aspectos motores, sociais, cognitivos e emocionais.", scale) }
                        item { GuideSubtitle("Etapas", scale) }
                        item { GuideTopic("Fase sensório-motora (0 a 2 anos);", scale) }
                        item { GuideTopic("Fase pré-operatória (2 a 7 anos );", scale) }
                        item { GuideTopic("Fase operatória concreta (7 a 11 anos);", scale) }
                        item { GuideTopic("Fase operatória formal (a partir 11 anos).", scale) }
                    }

                    9 -> {
                        item { Image(painter = painterResource(id = R.drawable.bg_hine_folder2), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillWidth) }
                    }

                }

                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }

    @Composable
    private fun GuideTopic(text: String, scale: Float = 1f, priority: Int = 0) {
        val prefix = when(priority){
            1 -> "○"
            2 -> "➤"
            else -> "●"
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Spacer(modifier = Modifier.width(((priority+1) * 24).dp))
            Text(text = prefix, textAlign = TextAlign.Left, fontSize = (scale * 14).sp, lineHeight = (scale * 15).sp, color = ColorGenderDark)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, textAlign = TextAlign.Left, fontSize = (scale * 14).sp, lineHeight = (scale * 15).sp, color = ColorGenderDark)
        }
    }

    @Composable
    private fun GuideParagraph(text: String, scale: Float = 1f) {
        Text(modifier = Modifier.padding(top = 8.dp), text = text, textAlign = TextAlign.Justify, fontSize = (scale * 16).sp, lineHeight = (scale * 17).sp, color = ColorGenderDark)
    }

    @Composable
    private fun GuideSubtitle(text: String, scale: Float = 1f) {
        Text(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), text = text, textAlign = TextAlign.Center, fontSize = (scale * 20).sp, fontWeight = FontWeight.Bold, lineHeight = (scale * 21).sp, color = ColorGenderDark)
    }

    @Composable
    private fun GuideTitle(text: String) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = ColorGender, shape = RoundedCornerShape(32.dp))
                .padding(8.dp), textAlign = TextAlign.Center, text = text, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold
        )
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
}
