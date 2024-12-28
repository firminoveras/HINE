package com.firmino.hinedigital.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmino.hinedigital.HINEApplication
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.Evaluation
import com.firmino.hinedigital.extensions.Exam
import com.firmino.hinedigital.extensions.evaluationsList
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.view.theme.ThemeGender
import com.firmino.hinedigital.view.theme.getColorTheme
import com.firmino.hinedigital.view.views.DialogTutorial
import com.firmino.hinedigital.viewmodel.EvaluationViewModel
import com.firmino.hinedigital.viewmodel.factory.EvaluationModelViewFactory
import kotlin.math.min

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
                viewModel.allEvaluations.observe(this@ExamActivity) { evaluations = it }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .background(ColorGenderLight)
                            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (evaluation != null) {
                            var evaluationModel by remember { mutableStateOf(evaluation) }
                            var key by remember { mutableStateOf(false) }
                            TopBackMenu(title = evaluationsList[evaluationIndex].section) { finish() }
                            EvaluationList(evaluationIndex = evaluationIndex, evaluations = evaluationsList, examIndex = examIndex, onExamChangeClick = {
                                if (it > 0) {
                                    if (examIndex < evaluationsList[evaluationIndex].exams.size - 1) examIndex++
                                } else {
                                    if (examIndex > 0) examIndex--
                                }
                            }) {
                                evaluationIndex = it
                                examIndex = 0
                            }
                            key(key) {
                                Content(
                                    evaluationModel = evaluationModel,
                                    evaluationIndex = evaluationIndex,
                                    examIndex = examIndex,
                                ) {
                                    viewModel.update(it)
                                    evaluationModel = it
                                    key = !key
                                }
                            }
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
        onEvaluationUpdate: (com.firmino.hinedigital.model.entity.Evaluation) -> Unit
    ) {
        val evaluation = evaluationsList[evaluationIndex]
        val exam = evaluation.exams[examIndex]
        val width = min(LocalConfiguration.current.screenWidthDp, LocalConfiguration.current.screenHeightDp) - 36
        val observationText = evaluationModel.comments[evaluationIndex][examIndex]
        val asymmetryText = evaluationModel.asymmetriesComments[evaluationIndex][examIndex]
        val hasAsymmetry = evaluationModel.asymmetries[evaluationIndex][examIndex] == 1

        var commentsVisible by remember(evaluationModel) { mutableStateOf(false) }

        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ExamImage(width, exam, evaluationModel.scores[evaluationIndex][examIndex])
                Spacer(Modifier.height(12.dp))
                ExamScale(exam, evaluationModel.scores[evaluationIndex][examIndex]) {
                    val copyScores = evaluationModel.scores.toMutableList()
                    copyScores[evaluationIndex][examIndex] = it
                    onEvaluationUpdate(evaluationModel.copy(scores = copyScores))
                }
            }
            CommentCard(
                modifier = Modifier.align(Alignment.BottomCenter),
                observationText = observationText,
                visible = commentsVisible,
                asymmetryText = asymmetryText,
                hasAsymmetry = hasAsymmetry,
                onVisibleUpdate = { commentsVisible = it },
            ) { comment, asymmetry, asymmetryComment ->
                val copyComments = evaluationModel.comments.toMutableList()
                copyComments[evaluationIndex][examIndex] = comment
                val copyAsymmetries = evaluationModel.asymmetries.toMutableList()
                copyAsymmetries[evaluationIndex][examIndex] = if (asymmetry) 1 else 0
                val copyAsymmetriesComments = evaluationModel.asymmetriesComments.toMutableList()
                copyAsymmetriesComments[evaluationIndex][examIndex] = asymmetryComment

                onEvaluationUpdate(
                    evaluationModel.copy(
                        comments = copyComments,
                        asymmetries = copyAsymmetries,
                        asymmetriesComments = copyAsymmetriesComments
                    )
                )
            }
        }
    }

    @Composable
    private fun CommentCard(
        modifier: Modifier,
        visible: Boolean,
        observationText: String,
        hasAsymmetry: Boolean,
        asymmetryText: String,
        onVisibleUpdate: (Boolean) -> Unit,
        onSaveClick: (comment: String, asymmetry: Boolean, asymmetryComment: String) -> Unit
    ) {
        var comment by remember { mutableStateOf(observationText) }
        var asymmetry by remember { mutableStateOf(false) }
        var asymmetryComment by remember { mutableStateOf(observationText) }
        var saveButtonVisible by remember { mutableStateOf(false) }

        comment = observationText
        asymmetry = hasAsymmetry
        asymmetryComment = asymmetryText
        saveButtonVisible = false

        Column(modifier = modifier) {
            Surface(
                modifier = Modifier.padding(horizontal = 18.dp),
                shape = RoundedCornerShape(topEnd = 18.dp, topStart = 18.dp),
                onClick = { onVisibleUpdate(!visible) },
                color = ColorGenderDark
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Icon(imageVector = if (visible) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp, contentDescription = null, tint = Color.White, modifier = Modifier.align(Alignment.CenterEnd))
                    Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_comments), contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(R.string.comments), fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            AnimatedVisibility(visible = visible) {
                Column(
                    Modifier
                        .padding(horizontal = 18.dp)
                        .background(color = Color.White)
                        .fillMaxWidth()
                        .padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = stringResource(R.string.obs), color = ColorGenderDark, fontWeight = FontWeight.Black)
                    TextField(
                        value = comment,
                        onValueChange = {
                            comment = it
                            saveButtonVisible = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = ColorGenderDarker,
                            unfocusedTextColor = ColorGenderDark,
                            focusedContainerColor = ColorGenderLight,
                            unfocusedContainerColor = ColorGenderLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                    )

                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.assimetrias), color = ColorGenderDark, fontWeight = FontWeight.Black)
                        Spacer(Modifier.width(12.dp))
                        Switch(
                            checked = asymmetry, onCheckedChange = {
                                asymmetry = !asymmetry
                                saveButtonVisible = true
                            }, colors = SwitchDefaults.colors(
                                checkedTrackColor = ColorGenderDark
                            )
                        )
                    }
                    AnimatedVisibility(visible = asymmetry) {
                        TextField(
                            value = asymmetryComment,
                            onValueChange = {
                                asymmetryComment = it
                                saveButtonVisible = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = ColorGenderDarker,
                                unfocusedTextColor = ColorGenderDark,
                                focusedContainerColor = ColorGenderLight,
                                unfocusedContainerColor = ColorGenderLight,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                        )
                    }
                    AnimatedVisibility(visible = saveButtonVisible) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onSaveClick(comment, asymmetry, if (asymmetry) asymmetryComment else "") },
                            colors = ButtonDefaults.textButtonColors(containerColor = ColorGenderDark, contentColor = Color.White)
                        ) { Text(text = "Salvar") }
                    }
                }
            }

        }
    }

    @Composable
    private fun ExamScale(exam: Exam, score: Int, onScoreChange: (newScore: Int) -> Unit) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (score > 0) onScoreChange(score - 1) },
                enabled = score > 0,
                colors = IconButtonDefaults.iconButtonColors(containerColor = ColorGenderDark, contentColor = Color.White, disabledContainerColor = ColorGender)
            ) {
                Text(text = "-", fontWeight = FontWeight.Black)
            }
            repeat(exam.maxScore) { index ->
                Icon(if (index < score) Icons.Rounded.Star else ImageVector.vectorResource(R.drawable.ic_star_border), contentDescription = null, tint = ColorGenderDark, modifier = Modifier.size(32.dp))
            }
            IconButton(
                onClick = { if (score < exam.maxScore) onScoreChange(score + 1) },
                enabled = score < exam.maxScore,
                colors = IconButtonDefaults.iconButtonColors(containerColor = ColorGenderDark, contentColor = Color.White, disabledContainerColor = ColorGender)
            ) {
                Text(text = "+", fontWeight = FontWeight.Black)
            }
        }
    }

    @Composable
    private fun ExamImage(width: Int, exam: Exam, score: Int) {

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

        val imageId = when (score) {
            0 -> if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId0.first.first else exam.imageId0.first.second
            1 -> if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId1.first.first else exam.imageId1.first.second
            2 -> if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId2.first.first else exam.imageId2.first.second
            3 -> if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId3.first.first else exam.imageId3.first.second
            4 -> if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId4.first.first else exam.imageId4.first.second
            else -> if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId5.first.first else exam.imageId5.first.second
        }
        val image2Id = when (score) {
            0 -> if (exam.imageId0.second != null) {
                if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId0.second!!.first else exam.imageId0.second!!.second
            } else null

            1 -> if (exam.imageId1.second != null) {
                if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId1.second!!.first else exam.imageId1.second!!.second
            } else null

            2 -> if (exam.imageId2.second != null) {
                if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId2.second!!.first else exam.imageId2.second!!.second
            } else null

            3 -> if (exam.imageId3.second != null) {
                if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId3.second!!.first else exam.imageId3.second!!.second
            } else null

            4 -> if (exam.imageId4.second != null) {
                if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId4.second!!.first else exam.imageId4.second!!.second
            } else null

            else -> if (exam.imageId5.second != null) {
                if (getColorTheme(this@ExamActivity) == ThemeGender.FEMALE) exam.imageId5.second!!.first else exam.imageId5.second!!.second
            } else null
        }

        val imageAltId = when (score) {
            0 -> exam.imageId0.third
            1 -> exam.imageId1.third
            2 -> exam.imageId2.third
            3 -> exam.imageId3.third
            4 -> exam.imageId4.third
            else -> exam.imageId5.third
        }
        var scoreTextExtended by remember(key1 = score, key2 = exam) { mutableStateOf(imageId == null) }
        var imageAlt by remember { mutableStateOf(false) }
        imageAlt = false
        ElevatedCard(shape = RoundedCornerShape(32.dp)) {
            Box(Modifier.size((width / 1.2f).dp)) {
                Image(painter = painterResource(id = R.drawable.bg_babybox), contentDescription = null, modifier = Modifier.fillMaxSize())
                if (imageId != null) {
                    Image(
                        painter = painterResource(id = if (imageAltId != null && imageAlt) imageAltId else imageId), contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp)
                            .alpha(if (image2Id != null && !imageAlt) alpha.value else 1f)
                            .padding(bottom = (width / if (scoreTextExtended) 1 else 4).dp)
                            .clip(shape = RoundedCornerShape(32.dp)),
                    )

                    if(image2Id != null && !imageAlt){
                        Image(
                            painter = painterResource(id = image2Id), contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(18.dp)
                                .alpha(1.0f - alpha.value)
                                .padding(bottom = (width / if (scoreTextExtended) 1 else 4).dp)
                                .clip(shape = RoundedCornerShape(32.dp)),
                        )
                    }
                }
                if (imageAltId != null && !scoreTextExtended) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                        onClick = { imageAlt = !imageAlt }) {
                        Icon(ImageVector.vectorResource(id = if (imageAlt) R.drawable.ic_image_original else R.drawable.ic_image_vector), contentDescription = null, tint = ColorGenderDark)
                    }
                }
                Surface(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .alpha(.7f)
                        .animateContentSize()
                        .fillMaxWidth()
                        .height((width / if (scoreTextExtended) 1 else 4).dp),
                    onClick = { scoreTextExtended = !scoreTextExtended },
                    color = Color.White
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        ElevatedAssistChip(
                            onClick = {},
                            label = { Text(text = "$score", fontWeight = FontWeight.Black) },
                            leadingIcon = { Icon(Icons.Rounded.Star, contentDescription = null, tint = Color.White) },
                            colors = AssistChipDefaults.elevatedAssistChipColors(containerColor = ColorGenderDark, labelColor = Color.White)
                        )
                        if (exam.getScoreTexts()[score].isNotBlank()) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .padding(bottom = 12.dp),
                                text = exam.getScoreTexts()[score].replace(";", "\n"),
                                overflow = TextOverflow.Ellipsis,
                                color = ColorGenderDarker,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TitleCard(exam: Exam) {
        var infoVisible by remember { mutableStateOf(false) }

        if (infoVisible) {
            DialogTutorial(
                title = exam.title,
                tutorial = exam.tutorial,
                onDismiss = { infoVisible = false },
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = exam.subtitle, textAlign = TextAlign.Center, color = ColorGenderDarker, style = MaterialTheme.typography.labelMedium)
            Row(modifier = Modifier.clickable { infoVisible = true }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                Icon(Icons.Rounded.Info, contentDescription = null, tint = ColorGenderDark, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text(text = stringResource(R.string.know_more), fontSize = 12.sp, color = ColorGenderDark)
            }
        }
    }

    @Composable
    private fun EvaluationList(examIndex: Int, evaluations: List<Evaluation>, onExamChangeClick: (indexDirection: Int) -> Unit, evaluationIndex: Int, onClick: (Int) -> Unit = {}) {
        val examActual = evaluations[evaluationIndex].exams[examIndex]
        val width = LocalConfiguration.current.screenWidthDp - 36
        Box(
            Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                Box {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .height((width / 24).dp)
                            .background(color = Color.White)
                            .align(Alignment.Center)
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf(R.drawable.ic_evaluation1, R.drawable.ic_evaluation2, R.drawable.ic_evaluation3, R.drawable.ic_evaluation4, R.drawable.ic_evaluation5, R.drawable.ic_evaluation6, R.drawable.ic_evaluation7).forEachIndexed { i, resource ->
                            Surface(
                                onClick = { onClick(i) },
                                color = if (evaluationIndex == i) ColorGenderDark else Color.White,
                                shape = CircleShape,
                                enabled = evaluationIndex != i
                            ) {
                                Image(
                                    painterResource(resource), contentDescription = null, modifier = Modifier
                                        .size((width / 8).dp)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height((width / 16).dp))
                Row(
                    Modifier
                        .background(color = ColorGenderDark, shape = RoundedCornerShape(32.dp))
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(modifier = Modifier.alpha(if (examIndex > 0) 1f else 0f), onClick = { onExamChangeClick(-1) }) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = null, tint = Color.White)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(text = evaluationsList[evaluationIndex].title.uppercase(), fontSize = 14.sp, color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, lineHeight = 14.sp)
                        Text(text = examActual.title, fontSize = 12.sp, color = Color.White, textAlign = TextAlign.Center, lineHeight = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            (0..<evaluations[evaluationIndex].exams.size).forEach {
                                Spacer(
                                    modifier = Modifier
                                        .size((if (it == examIndex) 8 else 6).dp)
                                        .background(color = if (it == examIndex) ColorGenderLight else ColorGenderDarker, shape = CircleShape)
                                )
                            }
                        }
                    }
                    IconButton(modifier = Modifier.alpha(if (examIndex < evaluations[evaluationIndex].exams.size - 1) 1f else 0f), onClick = { onExamChangeClick(1) }) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = Color.White)
                    }
                }
                Spacer(Modifier.height(16.dp))
                TitleCard(examActual)
            }
        }
    }

    @Composable
    private fun TopBackMenu(title: String, onClick: () -> Unit = {}) {

        Surface(
            modifier = Modifier
                .padding(horizontal = 18.dp),
            shape = RoundedCornerShape(bottomEnd = 18.dp, bottomStart = 18.dp),
            onClick = { onClick() },
            color = ColorGenderDark
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = title, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}