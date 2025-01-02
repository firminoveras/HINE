package com.firmino.hinedigital.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmino.hinedigital.HINEApplication
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.Header
import com.firmino.hinedigital.extensions.evaluationsImages
import com.firmino.hinedigital.extensions.evaluationsList
import com.firmino.hinedigital.model.entity.Evaluation
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.view.theme.ThemeGender
import com.firmino.hinedigital.view.theme.setColorTheme
import com.firmino.hinedigital.viewmodel.EvaluationViewModel
import com.firmino.hinedigital.viewmodel.factory.EvaluationModelViewFactory

class EvaluationMainActivity : ComponentActivity() {

    private val viewModel: EvaluationViewModel by viewModels { EvaluationModelViewFactory((application as HINEApplication).repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val id = intent.extras?.getInt("id") ?: -1
        setContent {
            HINEDigitalTheme {
                var evaluations by remember { mutableStateOf(listOf<Evaluation>()) }
                viewModel.allEvaluations.observe(this@EvaluationMainActivity) { evaluations = it }
                val evaluation = evaluations.firstOrNull { it.id == id }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (evaluation != null) setColorTheme(if (evaluation.gender == "feminino") ThemeGender.FEMALE else ThemeGender.MALE, LocalContext.current)
                    Column(
                        Modifier.padding(innerPadding).fillMaxSize().background(Brush.linearGradient(listOf(ColorGender, ColorGenderDark))),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Header(title = "Resumo Avaliativo", onBackPressed = {
                            startActivity(Intent(this@EvaluationMainActivity, ListEvaluationActivity::class.java))
                            finish()
                        })
                        if (evaluation != null) {
                            Text(text = evaluation.name, color = Color.White, style = MaterialTheme.typography.headlineSmall)
                            Text(text = evaluation.birthday, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            HorizontalDivider(Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp), color = Color.White)
                            Content(evaluation)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Content(evaluation: Evaluation) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.Top)) {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item { Spacer(modifier = Modifier.height(0.dp)) }
                    itemsIndexed(evaluationsList) { index, it ->
                        EvaluationItem(it, index, evaluation.getScoreByEvaluation(index), evaluation.getMaxScoreByEvaluation(index)) {
                            val intent = Intent(this@EvaluationMainActivity, ExamActivity::class.java)
                            intent.putExtra("index", index)
                            intent.putExtra("id", evaluation.id)
                            startActivity(intent)
                        }
                    }
                    item { Spacer(modifier = Modifier.height(120.dp)) }
                }
            }
            BottomBar(modifier = Modifier.align(Alignment.BottomCenter), evaluation)
        }
    }

    @Composable
    private fun EvaluationItem(data: com.firmino.hinedigital.extensions.Evaluation, index: Int, score: Int, maxScore: Int, onClick: () -> Unit) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.clickable { onClick() }) {
                Row(Modifier.heightIn(max = 72.dp), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .width(16.dp)
                            .weight(1f)
                            .fillMaxHeight()
                            .background(color = ColorGenderLight)
                    )
                    Image(
                        modifier = Modifier.padding(8.dp).weight(3f).aspectRatio(1f),
                        painter = painterResource(id = evaluationsImages[index]),
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier.padding(8.dp).weight(12f).fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "AVALIAÇÃO ${index + 1}", fontSize = 20.sp, color = ColorGenderDark, fontWeight = FontWeight.Black, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        Text(text = data.title, fontSize = 12.sp, color = ColorGenderDark)
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(5f).background(color = ColorGenderLight),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(if (data.scored) Icons.Rounded.Star else ImageVector.vectorResource(id = R.drawable.ic_star_border), contentDescription = null, tint = ColorGenderDark, modifier = Modifier.size(38.dp))
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.offset(y = (-4).dp)) {
                            Text(text = "$score", color = ColorGenderDarker, fontWeight = FontWeight.Black, fontSize = 20.sp)
                            Text(text = "/$maxScore", color = ColorGenderDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BottomBar(modifier: Modifier = Modifier, evaluation: Evaluation) {
        Column(modifier = modifier.fillMaxWidth()) {
            Spacer(
                Modifier
                    .height(6.dp)
                    .fillMaxWidth()
                    .background(brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Color(0.1f, 0.1f, 0.1f, 0.15f))))
            )
            Column(
                modifier = modifier.background(color = ColorGenderDark).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                    Score(icon = ImageVector.vectorResource(id = R.drawable.ic_star_border), value = evaluation.getComportamentalScore(), max = Evaluation.COMPORTAMENTAL_SCORE_MAX, title = "Comportamental")
                    Score(icon = Icons.Rounded.Star, scale = 1.2f, value = evaluation.getGlobalScore(), max = Evaluation.GLOBAL_SCORE_MAX, title = "Global")
                    Score(icon = ImageVector.vectorResource(id = R.drawable.ic_assymetry), value = evaluation.getAssymetriesCount(), max = Evaluation.ASSYMETRY_MAX, title = "Assimetrias")
                }

            }
        }
    }

    @Composable
    private fun Score(icon: ImageVector, value: Int, max: Int, title: String, scale: Float = 1.0f) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.Bottom) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size((32 * scale).dp))
                Text(text = value.toString(), color = Color.White, fontWeight = FontWeight.Black, fontSize = (22 * scale).sp)
                Text(text = "/$max", color = ColorGender, fontWeight = FontWeight.Black, fontSize = (18 * scale).sp)
            }
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = (11 * scale).sp)
        }
    }
}