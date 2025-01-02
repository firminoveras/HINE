package com.firmino.hinedigital.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.firmino.hinedigital.HINEApplication
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.Exam
import com.firmino.hinedigital.extensions.evaluationsImages
import com.firmino.hinedigital.extensions.evaluationsList
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.view.theme.getColorTheme
import com.firmino.hinedigital.view.views.EditTextDialog
import com.firmino.hinedigital.viewmodel.EvaluationViewModel
import com.firmino.hinedigital.viewmodel.factory.EvaluationModelViewFactory
import kotlinx.coroutines.launch

class ExamActivity : ComponentActivity() {

    private val viewModel: EvaluationViewModel by viewModels { EvaluationModelViewFactory((application as HINEApplication).repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HINEDigitalTheme {
                val index = intent.extras?.getInt("index")
                val id = intent.extras?.getInt("id")
                var evaluationIndex by remember { mutableIntStateOf(index ?: 0) }
                var examIndex by remember { mutableIntStateOf(0) }
                var evaluations by remember { mutableStateOf(listOf<com.firmino.hinedigital.model.entity.Evaluation>()) }
                val evaluation = evaluations.firstOrNull { it.id == id }
                var mapVisible by remember { mutableStateOf(true) }

                viewModel.allEvaluations.observe(this@ExamActivity) { evaluations = it }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding).background(ColorGenderLight).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        if (evaluation != null) {
                            var evaluationModel by remember { mutableStateOf(evaluation) }
                            EvaluationHeader(
                                evaluation = evaluation,
                                onBackButtonClick = { finish() },
                                content = {
                                    ToggleIcon(checked = mapVisible, icon = R.drawable.ic_map, onDarkSurface = true, onUpdate = { mapVisible = it })
                                }
                            )
                            Content(
                                evaluationModel = evaluationModel,
                                evaluationIndex = evaluationIndex,
                                examIndex = examIndex,
                                mapVisible = mapVisible,
                                onExamChange = { i, j ->
                                    evaluationIndex = i
                                    examIndex = j
                                },
                                onEvaluationUpdate = {
                                    viewModel.update(it)
                                    evaluationModel = it
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun EvaluationHeader(
        evaluation: com.firmino.hinedigital.model.entity.Evaluation?,
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

        Box(
            Modifier
                .shadow(elevation = 4.dp)
                .background(color = ColorGenderDark)
                .fillMaxWidth()
                .height(220.dp)
        ) {
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
                text = stringResource(R.string.subtitle_hine),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight(800),
                textAlign = TextAlign.End,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 12.dp, vertical = 56.dp)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd),
                content = content
            )
            if (evaluation != null) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        text = evaluation.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    listOf(
                        Triple("Nascimento", evaluation.birthday, ""),
                        Triple("Idade Corrigida/Gestacional", "${evaluation.correctedAge}/${evaluation.gestationalWeeks}", "semanas"),
                        Triple("Perimetro Cefálico", evaluation.cephalicSize, "cm"),
                    ).forEach {
                        Row {
                            Text(text = "${it.first}: ", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(text = "${it.second}", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Normal)
                            Text(text = " ${it.third}", style = MaterialTheme.typography.labelSmall, color = ColorGenderLight, fontWeight = FontWeight.Normal)
                        }
                    }
                }
            }
        }
    }
    @Composable
    private fun Content(
        evaluationModel: com.firmino.hinedigital.model.entity.Evaluation,
        evaluationIndex: Int,
        examIndex: Int,
        mapVisible: Boolean,
        onExamChange: (evaluation: Int, exam: Int) -> Unit,
        onEvaluationUpdate: (com.firmino.hinedigital.model.entity.Evaluation) -> Unit
    ) {
        val evaluation = evaluationsList[evaluationIndex]
        val exam = evaluation.exams[examIndex]

        var observationText by remember { mutableStateOf(evaluationModel.comments[evaluationIndex][examIndex]) }
        var asymmetryText by remember { mutableStateOf(evaluationModel.asymmetriesComments[evaluationIndex][examIndex]) }
        var infoVisible by remember { mutableStateOf(true) }
        var tutorialVisible by remember { mutableStateOf(false) }
        var observationVisible by remember { mutableStateOf(false) }
        var asymmetryVisible by remember { mutableStateOf(false) }
        var mapExpanded by remember { mutableStateOf(false) }
        var score by remember { mutableIntStateOf(evaluationModel.scores[evaluationIndex][examIndex]) }

        score = evaluationModel.scores[evaluationIndex][examIndex]
        if (mapExpanded && !mapVisible) mapExpanded = false

        observationText = evaluationModel.comments[evaluationIndex][examIndex]
        asymmetryText = evaluationModel.asymmetriesComments[evaluationIndex][examIndex]

        Box {
            Row(Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = !mapExpanded) {
                    Row {
                        LazyColumn(
                            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            item { Spacer(Modifier.height(12.dp)) }
                            item {
                                Row(modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    ActionIcon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, !(examIndex == 0 && evaluationIndex == 0)) {
                                        if (examIndex > 0) onExamChange(evaluationIndex, examIndex - 1)
                                        else onExamChange(evaluationIndex - 1, evaluationsList[evaluationIndex - 1].exams.size - 1)
                                    }
                                    ToggleIcon(infoVisible, R.drawable.ic_info) { infoVisible = it }
                                    ToggleIcon(tutorialVisible, R.drawable.ic_help) { tutorialVisible = it }
                                    ToggleIcon(observationVisible, R.drawable.ic_comment, notification = observationText.isNotBlank()) { observationVisible = it }
                                    ToggleIcon(asymmetryVisible, R.drawable.ic_assymetry, notification = asymmetryText.isNotBlank()) { asymmetryVisible = it }
                                    ActionIcon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, !(evaluationIndex == evaluationsList.size - 1 && examIndex == evaluationsList[evaluationIndex].exams.size - 1)) {
                                        if (examIndex < evaluationsList[evaluationIndex].exams.size - 1) onExamChange(evaluationIndex, examIndex + 1)
                                        else onExamChange(evaluationIndex + 1, 0)
                                    }
                                }
                            }
                            item { Column { AnimatedVisibility(visible = infoVisible) { ExamInfoView(evaluationIndex, examIndex) } } }
                            item { Column { AnimatedVisibility(visible = tutorialVisible) { ExamTutorialView(exam) } } }
                            item {
                                Column {
                                    AnimatedVisibility(visible = observationVisible) {
                                        CommentaryView(
                                            value = observationText,
                                            title = "Observações",
                                            icon = R.drawable.ic_comment,
                                            subtitle = "Adicione abaixo as observações da avaliação.",
                                            emptyText = "Nenhuma observação",
                                            onUpdate = {
                                                val copyComments = evaluationModel.comments.toMutableList()
                                                copyComments[evaluationIndex][examIndex] = it
                                                onEvaluationUpdate(evaluationModel.copy(comments = copyComments))
                                                observationText = it
                                            }
                                        )
                                    }
                                }
                            }
                            item {
                                Column {
                                    AnimatedVisibility(visible = asymmetryVisible) {
                                        CommentaryView(
                                            value = asymmetryText,
                                            title = "Assimetrias",
                                            icon = R.drawable.ic_assymetry,
                                            subtitle = "Adicione abaixo as observações das assimetrias.",
                                            emptyText = "Nenhuma assimetria",
                                            onUpdate = {
                                                val copyAsymmetries = evaluationModel.asymmetries.toMutableList()
                                                copyAsymmetries[evaluationIndex][examIndex] = if (it.isNotBlank()) 1 else 0
                                                val copyAsymmetriesComments = evaluationModel.asymmetriesComments.toMutableList()
                                                copyAsymmetriesComments[evaluationIndex][examIndex] = it
                                                onEvaluationUpdate(evaluationModel.copy(asymmetries = copyAsymmetries, asymmetriesComments = copyAsymmetriesComments))
                                                asymmetryText = it
                                            }
                                        )
                                    }
                                }
                            }
                            item {
                                Column {
                                    AnimatedVisibility(visible = infoVisible || tutorialVisible || observationVisible || asymmetryVisible) {
                                        HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp), color = ColorGender)
                                    }
                                }
                            }
                            item {
                                if (exam.subtitle.isNotBlank()) {
                                    Column(
                                        modifier = Modifier.padding(bottom = 12.dp).background(color = Color.White, shape = RoundedCornerShape(8.dp)).fillMaxWidth().padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AnimatedContent(targetState = exam.subtitle) {
                                            Text(text = it.capitalize(Locale.current), textAlign = TextAlign.Center, color = ColorGenderDarker, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                        }
                                    }
                                }
                            }
                            items(count = exam.maxScore + 1) {
                                ExamItem(
                                    exam = exam,
                                    model = evaluationModel,
                                    evaluationIndex = evaluationIndex,
                                    examIndex = examIndex,
                                    index = it,
                                    score = score,
                                    onScoreUpdate = { newScore -> score = newScore },
                                    onUpdate = onEvaluationUpdate
                                )
                            }
                        }
                        AnimatedVisibility(visible = mapVisible) {
                            Spacer(Modifier.width(50.dp))
                        }
                    }
                }
            }
            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                AnimatedVisibility(visible = mapVisible) {
                    ExamMap(evaluationIndex = evaluationIndex, examIndex = examIndex, expanded = mapExpanded, onExpandedUpdate = { mapExpanded = it }) { i, j -> onExamChange(i, j) }
                }
            }
        }
    }

    @Composable
    private fun ExamItem(
        exam: Exam,
        model: com.firmino.hinedigital.model.entity.Evaluation,
        evaluationIndex: Int,
        examIndex: Int,
        index: Int,
        score: Int,
        onScoreUpdate: (Int) -> Unit,
        onUpdate: (com.firmino.hinedigital.model.entity.Evaluation) -> Unit
    ) {
        val image1Id = exam.getFirstImage(index, getColorTheme(this@ExamActivity))
        val image2Id = exam.getSecondImage(index, getColorTheme(this@ExamActivity))
        val imageAltId = exam.getAltImage(index)

        var imageExpanded by remember { mutableStateOf(false) }
        var imageAlt by remember { mutableStateOf(false) }
        imageAlt = false

        val alpha = remember { Animatable(0f) }
        LaunchedEffect(true) {
            alpha.animateTo(
                targetValue = 1.0f, animationSpec = infiniteRepeatable(
                    repeatMode = RepeatMode.Reverse, animation = tween(
                        durationMillis = 2000, easing = EaseInOutSine
                    )
                )
            )
        }

        ElevatedCard(
            modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(containerColor = if (score == index) ColorGenderDark else Color.White),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min).clickable {
                    val copyScores = model.scores.toMutableList()
                    copyScores[evaluationIndex][examIndex] = index
                    onUpdate(model.copy(scores = copyScores))
                    onScoreUpdate(index)
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().weight(1f).background(color = if (score == index) ColorGenderDarker else ColorGenderDark).padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box {
                        if (index == 0) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_star_border),
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }
                        repeat(index) {
                            Icon(
                                modifier = Modifier.alpha(1f - (.2f * (index - it - 1))).padding(top = (2 * it).dp, start = (3 * it).dp).size(22.dp),
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                    Text(text = "Score $index", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.weight(3.5f)) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxWidth(if (image1Id == null) 1f else 0.7f).padding(8.dp),
                        text = exam.getScoreTexts()[index].replace("; ", "\n").ifBlank { "Nenhuma descrição" },
                        color = if (score == index) Color.White else if (exam.getScoreTexts()[index].isNotBlank()) ColorGenderDarker else ColorGenderDark,
                        fontStyle = if (exam.getScoreTexts()[index].isBlank()) FontStyle.Italic else FontStyle.Normal,
                        fontWeight = if (score == index) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (image1Id != null) {
                        ElevatedCard(
                            modifier = Modifier.align(Alignment.CenterStart).animateContentSize().fillMaxWidth(if (imageExpanded) 1f else 0.3f).aspectRatio(1f).padding(4.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Brush.verticalGradient(listOf(Color("#BCEBFF".toColorInt()), Color("#FDCBFF".toColorInt()))))
                                    .fillMaxSize()
                                    .clickable { imageExpanded = !imageExpanded }
                            ) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .alpha(if (image2Id != null && !imageAlt) alpha.value else 1f)
                                        .padding(12.dp),
                                    painter = painterResource(id = if (imageAltId != null && imageAlt) imageAltId else image1Id),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                )
                                if (image2Id != null && !imageAlt) {
                                    Image(
                                        modifier = Modifier.fillMaxSize().padding(12.dp).alpha(1.0f - alpha.value),
                                        painter = painterResource(id = image2Id), contentDescription = null,
                                    )
                                }
                                if (imageAltId != null && imageExpanded) {
                                    IconButton(
                                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                                        onClick = { imageAlt = !imageAlt })
                                    {
                                        Icon(ImageVector.vectorResource(id = if (imageAlt) R.drawable.ic_image_original else R.drawable.ic_image_vector), contentDescription = null, tint = ColorGenderDark)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ExamTutorialView(exam: Exam) {
        var index by remember { mutableIntStateOf(0) }
        val textParagraphs = exam.tutorial.split(". ")
        Column(
            modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(8.dp)).padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row {
                Icon(ImageVector.vectorResource(R.drawable.ic_help), contentDescription = null, tint = ColorGenderDark, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = "Tutorial", textAlign = TextAlign.Center, color = ColorGenderDark, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }
            if (exam.tutorial.isNotBlank()) {
                AnimatedContent(targetState = index) {
                    Text(text = textParagraphs[it] + ".", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = ColorGenderDarker)
                }
                if (textParagraphs.size > 1) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (index > 0) index-- }) {
                            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = null, tint = ColorGenderDark)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
                            textParagraphs.indices.forEach { i ->
                                Spacer(
                                    modifier = Modifier
                                        .size((if (i == index) 8 else 6).dp)
                                        .background(color = if (i == index) ColorGenderDarker else ColorGenderDark, shape = CircleShape)
                                )
                            }
                        }
                        IconButton(onClick = { if (index < textParagraphs.size - 1) index++ }) {
                            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = ColorGenderDark)
                        }
                    }
                }
            } else {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Nenhum tutorial",
                    textAlign = TextAlign.Center,
                    color = ColorGender,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

    @Composable
    private fun ExamInfoView(evaluationIndex: Int, examIndex: Int) {
        Column(
            modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(8.dp)).padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(ImageVector.vectorResource(R.drawable.ic_info), contentDescription = null, tint = ColorGenderDark, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = "Informações do Exame", textAlign = TextAlign.Center, color = ColorGenderDark, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }
            if (evaluationsList[evaluationIndex].section.split(":")[1].trim() == evaluationsList[evaluationIndex].title.trim()) {
                ExamInfoViewTextSection(
                    title = "${evaluationsList[evaluationIndex].section.split(":")[0].trim()} - Avaliação ${evaluationIndex + 1}",
                    text = evaluationsList[evaluationIndex].title
                )
            } else {
                ExamInfoViewTextSection(
                    title = evaluationsList[evaluationIndex].section.split(":")[0].trim(),
                    text = evaluationsList[evaluationIndex].section.split(":")[1].trim()
                )
                ExamInfoViewTextSection(
                    title = "Avaliação ${evaluationIndex + 1}",
                    text = evaluationsList[evaluationIndex].title
                )
            }
            ExamInfoViewTextSection(
                title = "Exame ${examIndex + 1}",
                text = evaluationsList[evaluationIndex].exams[examIndex].title
            )
        }
    }

    @Composable
    private fun ExamInfoViewTextSection(title: String, text: String) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedContent(targetState = title) {
                Text(
                    modifier = Modifier.background(color = ColorGenderDark, shape = RoundedCornerShape(4.dp)).padding(horizontal = 4.dp),
                    text = it,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            AnimatedContent(targetState = text) {
                Text(
                    text = it,
                    color = ColorGenderDarker,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    @Composable
    private fun ActionIcon(icon: ImageVector, enabled: Boolean, onClick: () -> Unit) {
        Row {
            IconButton(
                onClick = onClick,
                enabled = enabled,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = ColorGenderDark,
                    containerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Icon(icon, null)
            }
        }
    }

    @Composable
    private fun ToggleIcon(checked: Boolean, icon: Int, notification: Boolean = false, onDarkSurface: Boolean = false, onUpdate: (Boolean) -> Unit) {
        Row {
            IconToggleButton(
                checked = checked,
                onCheckedChange = { onUpdate(it) },
                colors = IconButtonDefaults.iconToggleButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = if (onDarkSurface) Color.White else ColorGender,
                    checkedContentColor = ColorGenderDark,
                    checkedContainerColor = Color.White
                )
            ) {
                BadgedBox(badge = { if (notification) Badge() }) {
                    Icon(ImageVector.vectorResource(icon), null)
                }
            }
        }
    }

    @Composable
    private fun ExamMap(
        modifier: Modifier = Modifier,
        evaluationIndex: Int,
        examIndex: Int,
        expanded: Boolean,
        onExpandedUpdate: (Boolean) -> Unit, onUpdate: (Int, Int) -> Unit
    ) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(evaluationIndex) {
            coroutineScope.launch {
                listState.animateScrollToItem(evaluationIndex)
            }
        }
        Box(modifier = modifier.fillMaxHeight().padding(end = 8.dp)) {
            LazyColumn(state = listState, horizontalAlignment = Alignment.End) {
                evaluationsList.forEachIndexed { i, evaluation ->
                    item {
                        Column(horizontalAlignment = Alignment.End) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AnimatedVisibility(visible = expanded) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Avaliação ${i + 1}",
                                            color = ColorGenderDarker,
                                            fontWeight = FontWeight.Black,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = evaluationsList[i].title,
                                            color = ColorGenderDarker,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                                IconButton(onClick = { onExpandedUpdate(!expanded) }) {
                                    Image(painterResource(evaluationsImages[i]), modifier = Modifier.padding(6.dp), contentDescription = null)
                                }
                            }

                            evaluation.exams.forEachIndexed { j, _ ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    AnimatedVisibility(visible = expanded) {
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "Exame ${j + 1}",
                                                color = ColorGenderDark,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = evaluationsList[i].exams[j].title,
                                                color = ColorGenderDark,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            onUpdate(i, j)
                                            onExpandedUpdate((i == evaluationIndex && j == examIndex).let { if (it) !expanded else false })
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = if (i == evaluationIndex && j == examIndex) Color.White else Color.Transparent,
                                            contentColor = if (i == evaluationIndex && j == examIndex) ColorGenderDark else ColorGender
                                        ),
                                    ) {
                                        Icon(ImageVector.vectorResource(R.drawable.ic_dot), contentDescription = null, modifier = Modifier.size(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun CommentaryView(
        value: String,
        title: String,
        icon: Int,
        subtitle: String,
        emptyText: String,
        onUpdate: (result: String) -> Unit
    ) {
        var editorVisible by remember { mutableStateOf(false) }

        if (editorVisible) {
            EditTextDialog(
                title = title,
                value = value,
                icon = icon,
                subtitle = subtitle,
                onDismiss = { editorVisible = false },
                onConfirm = {
                    editorVisible = false
                    onUpdate(it)
                }
            )
        }

        Column(modifier = Modifier.padding(bottom = 12.dp).background(color = Color.White, shape = RoundedCornerShape(8.dp)).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(48.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(ImageVector.vectorResource(icon), contentDescription = null, tint = ColorGenderDark, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            color = ColorGenderDark,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (value.isNotBlank()) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = value,
                            textAlign = TextAlign.Center,
                            color = ColorGenderDarker,
                            style = MaterialTheme.typography.labelMedium
                        )
                    } else {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = emptyText,
                            textAlign = TextAlign.Center,
                            color = ColorGender,
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                IconButton(onClick = { editorVisible = true }) {
                    Icon(if (value.isNotBlank()) Icons.Rounded.Edit else Icons.Rounded.Add, contentDescription = null, tint = ColorGenderDark)
                }
            }
        }
    }
}