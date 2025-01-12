package com.firmino.hinedigital.view.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.Header
import com.firmino.hinedigital.extensions.toBrazilianDateFormat
import com.firmino.hinedigital.model.entity.Evaluation
import com.firmino.hinedigital.view.MainActivity
import com.firmino.hinedigital.view.theme.ColorBlue
import com.firmino.hinedigital.view.theme.ColorBlueDark
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.ColorPink
import com.firmino.hinedigital.view.theme.ColorPinkDark
import com.firmino.hinedigital.viewmodel.EvaluationViewModel
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDAcroForm
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.min

private var uriId by mutableStateOf(Uri.EMPTY)

@Composable
fun EvaluationListScreen(navController: NavController, viewModel: EvaluationViewModel) {
    val context = LocalContext.current
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(ColorGender, ColorGenderDark)))
        ) {
            Header(title = stringResource(R.string.last_evaluations), onBackPressed = { navController.popBackStack() })
            Spacer(modifier = Modifier.height(24.dp))
            Content(viewModel = viewModel, context = context, navController = navController)
        }
    }
}

@Composable
private fun Content(viewModel: EvaluationViewModel, context: Context, navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var deleteId by remember { mutableIntStateOf(-1) }
    var downloadId by remember { mutableIntStateOf(-1) }

    var evaluations by remember { mutableStateOf(listOf<Evaluation>()) }
    viewModel.allEvaluations.observe(context as MainActivity) { evaluations = it }

    if (deleteId >= 0) {
        DialogConfirm(
            title = stringResource(R.string.delete_evaluation),
            text = stringResource(R.string.delete_evaluation_text),
            icon = Icons.Rounded.Delete,
            confirmText = stringResource(R.string.yes_delete),
            onDismiss = { deleteId = -1 }
        ) {
            viewModel.delete(deleteId)
            deleteId = -1
        }
    }

    if (downloadId != -1) {
        DialogDownload(
            uri = uriId,
            onGenerate = {
                val evaluation = evaluations.find { it.id == downloadId }
                if (evaluation != null) downloadEvaluationPdf(evaluation, context)
            },
            onDismiss = {
                uriId = Uri.EMPTY
                downloadId = -1
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.Top)
    ) {
        SearchEvaluations(onUpdate = { searchText = it }, value = searchText, onClear = { searchText = "" })

        if (evaluations.none { it.name.contains(searchText.trim(), true) }) {
            EmptyContainer(query = searchText.trim())
        } else {
            LazyColumn(Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                items(evaluations.filter { it.name.contains(searchText.trim(), true) || searchText.isEmpty() }, key = { it.id }) {
                    var showDialogEdit by remember { mutableStateOf(false) }
                    if (showDialogEdit) EditDialog(it, onDismiss = { showDialogEdit = false }, onConfirm = { newEvaluation ->
                        showDialogEdit = false
                        viewModel.update(newEvaluation)
                    })

                    EvaluationListView(
                        evaluation = it,
                        onDelete = { deleteId = it.id },
                        onEdit = { showDialogEdit = true },
                        onDownload = { downloadId = it.id },
                        onStart = {
                            navController.navigate("evaluationResume/${it.id}")
                        }
                    )
                }
                item {
                    ListFooter(evaluations.size)
                }
            }
        }
    }
}

private fun downloadEvaluationPdf(evaluation: Evaluation, context: Context, comments: String = "") {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm:ss")
            val formattedDateTime = now.format(formatter)

            val assets = context.assets
            val modelName = "model.pdf"

            val file = File(context.cacheDir, "Avaliacao_HINE.pdf")
            if (!file.exists()) file.createNewFile()
            val uri = FileProvider.getUriForFile(context, "com.firmino.hinedigital.fileprovider", file)

            val inputStream = assets.open(modelName)
            file.outputStream().use { output -> inputStream.copyTo(output) }
            inputStream.close()

            PDFBoxResourceLoader.init(context.applicationContext)

            val document: PDDocument = PDDocument.load(file)
            val acroForm: PDAcroForm = document.getDocumentCatalog().acroForm
            val fields: List<PDField> = acroForm.getFields()

            for (field in fields) {
                when (field.fullyQualifiedName) {
                    "name" -> field.setValue(evaluation.name)
                    "birthday" -> field.setValue(evaluation.birthday)
                    "gestationalWeeks" -> field.setValue("${evaluation.gestationalWeeks} semanas")
                    "today" -> field.setValue(formattedDateTime)
                    "correctedAge" -> field.setValue("${evaluation.correctedAge} semanas")
                    "cephalicSize" -> field.setValue("${evaluation.cephalicSize} cm")
                    "globalScore" -> field.setValue(evaluation.getGlobalScore().toString())
                    "asymmetriesCount" -> field.setValue(evaluation.getAssymetriesCount().toString())
                    "comportmentScore" -> field.setValue(evaluation.getComportamentalScore().toString())
                    "scoreByEvaluation0" -> field.setValue(evaluation.getScoreByEvaluation(0).toString())
                    "scoreByEvaluation1" -> field.setValue(evaluation.getScoreByEvaluation(1).toString())
                    "scoreByEvaluation2" -> field.setValue(evaluation.getScoreByEvaluation(2).toString())
                    "scoreByEvaluation3" -> field.setValue(evaluation.getScoreByEvaluation(3).toString())
                    "scoreByEvaluation4" -> field.setValue(evaluation.getScoreByEvaluation(4).toString())
                    "comments" -> field.setValue(comments)
                    "s00" -> field.setValue(evaluation.scores[0][0].toString())
                    "s01" -> field.setValue(evaluation.scores[0][1].toString())
                    "s02" -> field.setValue(evaluation.scores[0][2].toString())
                    "s03" -> field.setValue(evaluation.scores[0][3].toString())
                    "s04" -> field.setValue(evaluation.scores[0][4].toString())
                    "s10" -> field.setValue(evaluation.scores[1][0].toString())
                    "s11" -> field.setValue(evaluation.scores[1][1].toString())
                    "s12" -> field.setValue(evaluation.scores[1][2].toString())
                    "s13" -> field.setValue(evaluation.scores[1][3].toString())
                    "s14" -> field.setValue(evaluation.scores[1][4].toString())
                    "s15" -> field.setValue(evaluation.scores[1][5].toString())
                    "s20" -> field.setValue(evaluation.scores[2][0].toString())
                    "s21" -> field.setValue(evaluation.scores[2][1].toString())
                    "s30" -> field.setValue(evaluation.scores[3][0].toString())
                    "s31" -> field.setValue(evaluation.scores[3][1].toString())
                    "s32" -> field.setValue(evaluation.scores[3][2].toString())
                    "s33" -> field.setValue(evaluation.scores[3][3].toString())
                    "s34" -> field.setValue(evaluation.scores[3][4].toString())
                    "s35" -> field.setValue(evaluation.scores[3][5].toString())
                    "s36" -> field.setValue(evaluation.scores[3][6].toString())
                    "s37" -> field.setValue(evaluation.scores[3][7].toString())
                    "s40" -> field.setValue(evaluation.scores[4][0].toString())
                    "s41" -> field.setValue(evaluation.scores[4][1].toString())
                    "s42" -> field.setValue(evaluation.scores[4][2].toString())
                    "s43" -> field.setValue(evaluation.scores[4][3].toString())
                    "s44" -> field.setValue(evaluation.scores[4][4].toString())
                    "c00" -> field.setValue(evaluation.comments[0][0] + if (evaluation.asymmetries[0][0] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[0][0]}" else "")
                    "c01" -> field.setValue(evaluation.comments[0][1] + if (evaluation.asymmetries[0][1] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[0][1]}" else "")
                    "c02" -> field.setValue(evaluation.comments[0][2] + if (evaluation.asymmetries[0][2] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[0][2]}" else "")
                    "c03" -> field.setValue(evaluation.comments[0][3] + if (evaluation.asymmetries[0][3] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[0][3]}" else "")
                    "c04" -> field.setValue(evaluation.comments[0][4] + if (evaluation.asymmetries[0][4] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[0][4]}" else "")
                    "c10" -> field.setValue(evaluation.comments[1][0] + if (evaluation.asymmetries[1][0] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[1][0]}" else "")
                    "c11" -> field.setValue(evaluation.comments[1][1] + if (evaluation.asymmetries[1][1] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[1][1]}" else "")
                    "c12" -> field.setValue(evaluation.comments[1][2] + if (evaluation.asymmetries[1][2] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[1][2]}" else "")
                    "c13" -> field.setValue(evaluation.comments[1][3] + if (evaluation.asymmetries[1][3] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[1][3]}" else "")
                    "c14" -> field.setValue(evaluation.comments[1][4] + if (evaluation.asymmetries[1][4] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[1][4]}" else "")
                    "c15" -> field.setValue(evaluation.comments[1][5] + if (evaluation.asymmetries[1][5] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[1][5]}" else "")
                    "c20" -> field.setValue(evaluation.comments[2][0] + if (evaluation.asymmetries[2][0] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[2][0]}" else "")
                    "c21" -> field.setValue(evaluation.comments[2][1] + if (evaluation.asymmetries[2][1] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[2][1]}" else "")
                    "c30" -> field.setValue(evaluation.comments[3][0] + if (evaluation.asymmetries[3][0] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[3][0]}" else "")
                    "c31" -> field.setValue(evaluation.comments[3][1] + if (evaluation.asymmetries[3][1] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[3][1]}" else "")
                    "c32" -> field.setValue(evaluation.comments[3][2] + if (evaluation.asymmetries[3][2] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[3][2]}" else "")
                    "c33" -> field.setValue(evaluation.comments[3][3] + if (evaluation.asymmetries[3][3] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[3][3]}" else "")
                    "c34" -> field.setValue(evaluation.comments[3][4] + if (evaluation.asymmetries[3][4] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[3][4]}" else "")
                    "c35" -> field.setValue(evaluation.comments[3][5] + if (evaluation.asymmetries[3][5] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[3][5]}" else "")
                    "c36" -> field.setValue(evaluation.comments[3][6] + if (evaluation.asymmetries[3][6] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[3][6]}" else "")
                    "c37" -> field.setValue(evaluation.comments[3][7] + if (evaluation.asymmetries[3][7] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[3][7]}" else "")
                    "c40" -> field.setValue(evaluation.comments[4][0] + if (evaluation.asymmetries[4][0] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[4][0]}" else "")
                    "c41" -> field.setValue(evaluation.comments[4][1] + if (evaluation.asymmetries[4][1] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[4][1]}" else "")
                    "c42" -> field.setValue(evaluation.comments[4][2] + if (evaluation.asymmetries[4][2] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[4][2]}" else "")
                    "c43" -> field.setValue(evaluation.comments[4][3] + if (evaluation.asymmetries[4][3] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[4][3]}" else "")
                    "c44" -> field.setValue(evaluation.comments[4][4] + if (evaluation.asymmetries[4][4] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[4][4]}" else "")
                    "c50" -> field.setValue("Marco: ${evaluation.scores[5][0]}\n" + evaluation.comments[5][0] + if (evaluation.asymmetries[5][0] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[5][0]}" else "")
                    "c51" -> field.setValue("Marco: ${evaluation.scores[5][1]}\n" + evaluation.comments[5][1] + if (evaluation.asymmetries[5][1] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[5][1]}" else "")
                    "c52" -> field.setValue("Marco: ${evaluation.scores[5][2]}\n" + evaluation.comments[5][2] + if (evaluation.asymmetries[5][2] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[5][2]}" else "")
                    "c53" -> field.setValue("Marco: ${evaluation.scores[5][3]}\n" + evaluation.comments[5][3] + if (evaluation.asymmetries[5][3] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[5][3]}" else "")
                    "c54" -> field.setValue("Marco: ${evaluation.scores[5][4]}\n" + evaluation.comments[5][4] + if (evaluation.asymmetries[5][4] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[5][4]}" else "")
                    "c55" -> field.setValue("Marco: ${evaluation.scores[5][5]}\n" + evaluation.comments[5][5] + if (evaluation.asymmetries[5][5] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[5][5]}" else "")
                    "c56" -> field.setValue("Marco: ${evaluation.scores[5][6]}\n" + evaluation.comments[5][6] + if (evaluation.asymmetries[5][6] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[5][6]}" else "")
                    "c57" -> field.setValue("Marco: ${evaluation.scores[5][7]}\n" + evaluation.comments[5][7] + if (evaluation.asymmetries[5][7] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[5][7]}" else "")
                    "c60" -> field.setValue("Comportamento: ${evaluation.scores[6][0] + 1}\n" + evaluation.comments[6][0] + if (evaluation.asymmetries[6][0] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[6][0]}" else "")
                    "c61" -> field.setValue("Comportamento: ${evaluation.scores[6][1] + 1}\n" + evaluation.comments[6][1] + if (evaluation.asymmetries[6][1] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[6][1]}" else "")
                    "c62" -> field.setValue("Comportamento: ${evaluation.scores[6][2] + 1}\n" + evaluation.comments[6][2] + if (evaluation.asymmetries[6][2] == 1) "\nAssimetria\n${evaluation.asymmetriesComments[6][2]}" else "")
                }
            }

            acroForm.flatten()
            document.save(file)
            document.close()

            uriId = uri

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun EmptyContainer(query: String = "") {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.95f)
            .padding(horizontal = 64.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(bitmap = ImageBitmap.imageResource(id = R.drawable.bg_empty), contentDescription = null, contentScale = ContentScale.Inside)
        Text(text = stringResource(R.string.empty), color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Black)
        Text(text = stringResource(R.string.empty_text), color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Center)
        if (query.isNotBlank()) Text(text = "para a busca de \"$query\"", color = Color.White, fontSize = 16.sp)
    }
}

@Composable
private fun ListFooter(size: Int) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = 48.dp)
                .background(color = Color.White)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "$size ite${if (size > 1) "ns" else "m"} encontrado${if (size > 1) "s" else ""}.", style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.White)
    }
}

@Composable
private fun EvaluationListView(
    evaluation: Evaluation,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDownload: () -> Unit = {},
    onStart: () -> Unit = {},
) {
    val color = if (evaluation.gender == "feminino") ColorPink else ColorBlue
    var extended by remember { mutableStateOf(false) }
    Column {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.clickable { onStart() }) {
                Row(Modifier.heightIn(max = 72.dp)) {
                    Spacer(modifier = Modifier.width(16.dp).weight(1f).fillMaxHeight().background(color = color))
                    Column(
                        Modifier
                            .padding(8.dp)
                            .weight(15f)
                            .fillMaxHeight(), verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = evaluation.name,
                            fontSize = 18.sp,
                            color = ColorGenderDark,
                            fontWeight = FontWeight.Black,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Surface(
                            onClick = { extended = !extended },
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = if (extended) "Recolher" else "Expandir", fontSize = 14.sp, color = ColorGenderDark)
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = if (extended) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = ColorGenderDark
                                )
                            }
                        }
                    }
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .weight(4f)
                            .background(color = ColorGenderLight), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Rounded.Star, contentDescription = null, tint = ColorGenderDark, modifier = Modifier.size(38.dp))
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.offset(y = (-4).dp)) {
                            Text(text = "${evaluation.getGlobalScore()}", color = ColorGenderDarker, fontWeight = FontWeight.Black, fontSize = 20.sp)
                            Text(text = "/${Evaluation.GLOBAL_SCORE_MAX}", color = ColorGenderDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }

                AnimatedVisibility(visible = extended) {
                    Column {
                        listOf("Função dos nervos cranianos", "Postura", "Movimentos", "Tônus", "Reflexos e reações").forEachIndexed { index, s ->
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .background(color = ColorGender)
                            )
                            Row(Modifier.heightIn(max = 32.dp), verticalAlignment = Alignment.CenterVertically) {
                                Spacer(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .background(color = color)
                                )
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp).weight(15f),
                                    text = s,
                                    fontSize = 14.sp,
                                    color = ColorGenderDark,
                                )
                                Row(
                                    modifier = Modifier.fillMaxHeight().weight(4f).background(color = ColorGenderLight),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "${evaluation.getScoreByEvaluation(index)}", color = ColorGenderDarker, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text(text = "/%02d".format(evaluation.getMaxScoreByEvaluation(index)), color = ColorGenderDark, fontWeight = FontWeight.Normal, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = extended) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ElevatedButton(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 16.dp),
                    onClick = { onDelete() }, colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = ColorGenderDark
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                }
                ElevatedButton(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    onClick = { onEdit() }, colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = ColorGenderDark
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }
                ElevatedButton(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomEnd = 16.dp, bottomStart = 8.dp),
                    onClick = { onDownload() }, colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = ColorGenderDark
                    )
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_download), contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun SearchEvaluations(
    maxLength: Int = 256,
    value: String = "",
    onClear: () -> Unit = {},
    onUpdate: (value: String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        value = value,
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = ColorGenderDark
            )
        },
        trailingIcon = {
            if (value.isNotBlank()) Icon(
                imageVector = Icons.Rounded.Clear,
                contentDescription = null,
                tint = ColorGenderDark,
                modifier = Modifier.clickable { onClear() })
        },
        onValueChange = {
            onUpdate(it.substring(0, min(it.length, maxLength)))
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrectEnabled = false,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
        }),
        colors = TextFieldDefaults.colors(
            focusedTextColor = ColorGenderDark,
            unfocusedTextColor = ColorGender,
            cursorColor = ColorGender,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(120.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.titleMedium,
    )
}

@Composable
fun DialogDownload(
    uri: Uri,
    onGenerate: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }

    if (uri.toString().isNotBlank()) loading = false

    Dialog(
        onDismissRequest = {
            loading = false
            onDismiss()
        },
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_download), contentDescription = null, tint = ColorGenderDark)
                Spacer(Modifier.height(16.dp))
                Text(text = "Exportar Avaliação (PDF)", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Text(text = stringResource(R.string.pdf_view_text), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Justify)

                Spacer(Modifier.height(24.dp))
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(42.dp),
                        color = ColorGender,
                        strokeWidth = 12.dp,
                        trackColor = ColorGenderDark,
                    )
                    Text(text = "Gerando PDF...", style = MaterialTheme.typography.labelSmall)
                } else {
                    if (uri.toString().isNotBlank()) {
                        loading = false
                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(uri, "application/pdf")
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            context.startActivity(intent)
                        }, colors = ButtonDefaults.textButtonColors(containerColor = ColorGenderDark, contentColor = Color.White)) {
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_open_file), contentDescription = null, Modifier.size(24.dp))
                            Spacer(Modifier.width(16.dp))
                            Text("Abrir")
                        }
                        Button(onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.type = "application/pdf"
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                            context.startActivity(Intent.createChooser(shareIntent, "Compartilhar arquivo"))
                        }, colors = ButtonDefaults.textButtonColors(containerColor = ColorGenderDark, contentColor = Color.White)) {
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_share), contentDescription = null, Modifier.size(24.dp))
                            Spacer(Modifier.width(16.dp))
                            Text("Compartilhar")
                        }
                    } else {
                        Button(onClick = {
                            loading = true
                            onGenerate()
                        }, colors = ButtonDefaults.textButtonColors(containerColor = ColorGenderDark, contentColor = Color.White)) {
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_pdf), contentDescription = null, Modifier.size(24.dp))
                            Spacer(Modifier.width(16.dp))
                            Text("Gerar PDF")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditDialog(evaluation: Evaluation, onDismiss: () -> Unit = {}, onConfirm: (evaluation: Evaluation) -> Unit = {}) {
    var name by remember { mutableStateOf(evaluation.name) }
    var gender by remember { mutableStateOf(evaluation.gender) }
    var birthday by remember { mutableStateOf(evaluation.birthday) }
    var gestationalWeeks by remember { mutableStateOf(evaluation.gestationalWeeks.toString()) }
    var correctedAge by remember { mutableStateOf(evaluation.correctedAge.toString()) }
    var cephalicSize by remember { mutableStateOf(evaluation.cephalicSize.toString()) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showDatePickerDialog) {
        DatePickerModal(
            onDateSelected = { millis -> birthday = millis?.toBrazilianDateFormat() ?: "" },
            onDismiss = { showDatePickerDialog = false }
        )
    }

    if (showConfirmDialog) {
        DialogConfirm(
            title = "Confirmar alteraçoes?",
            text = "Os dados deste indivíduo serão substituídos.",
            onDismiss = { showConfirmDialog = false }
        ) {
            showConfirmDialog = false
            onConfirm(
                evaluation.copy(
                    name = name,
                    gender = gender,
                    birthday = birthday,
                    gestationalWeeks = gestationalWeeks.toInt(),
                    correctedAge = correctedAge.toInt(),
                    cephalicSize = cephalicSize.toInt()
                )
            )
        }
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = ColorGenderDark)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Edit, contentDescription = null, tint = Color.White)
                    Text(text = "Editar Avaliação", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.titleLarge, color = Color.White)
                }
                EditDialogTextField(value = name, onValueChange = { name = it }, label = LocalContext.current.getString(R.string.name), keyboardType = KeyboardType.Text) {
                    Icon(
                        ImageVector.vectorResource(id = if (gender == "feminino") R.drawable.ic_female else R.drawable.ic_male), contentDescription = null,
                        tint = if (gender == "feminino") ColorPinkDark else ColorBlueDark, modifier = Modifier.clickable {
                            gender = if (gender == "feminino") "masculino" else "feminino"
                        })
                }
                EditDialogTextField(value = birthday, onValueChange = { birthday = it }, label = LocalContext.current.getString(R.string.born_date), maxLength = 10, isEditable = false) {
                    Icon(Icons.Rounded.DateRange, contentDescription = null, tint = ColorGenderDark, modifier = Modifier.clickable {
                        showDatePickerDialog = true
                    })
                }
                EditDialogTextField(value = gestationalWeeks, onValueChange = { gestationalWeeks = it }, label = LocalContext.current.getString(R.string.gestational_year), maxLength = 4) {
                    Text(text = "Semanas", color = ColorGenderDark, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(end = 8.dp))
                }
                EditDialogTextField(value = correctedAge, onValueChange = { correctedAge = it }, label = LocalContext.current.getString(R.string.corrected_age), maxLength = 4) {
                    Text(text = "Semanas", color = ColorGenderDark, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(end = 8.dp))
                }
                EditDialogTextField(value = cephalicSize, onValueChange = { cephalicSize = it }, label = LocalContext.current.getString(R.string.cephalic_size), maxLength = 4) {
                    Text(text = "cm", color = ColorGenderDark, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(end = 8.dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) { Text(text = "Cancelar", color = Color.White, fontWeight = FontWeight.Normal) }
                    TextButton(onClick = { showConfirmDialog = true }) { Text(text = "Salvar", color = Color.White, fontWeight = FontWeight.Black) }
                }
            }
        }
    }
}

@Composable
private fun EditDialogTextField(value: String, isEditable: Boolean = true, onValueChange: (newValue: String) -> Unit, label: String, maxLength: Int = 256, keyboardType: KeyboardType = KeyboardType.Number, content: @Composable () -> Unit = {}) {
    Column {
        TextField(
            label = {
                Text(text = label, color = ColorGender, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            },
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = {
                if (isEditable) onValueChange(it.substring(0, min(it.length, maxLength)))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next,
                keyboardType = keyboardType
            ),
            colors = TextFieldDefaults.colors(
                selectionColors = TextSelectionColors(handleColor = ColorGender, backgroundColor = ColorGenderLight),
                focusedTextColor = ColorGenderDarker,
                unfocusedTextColor = ColorGenderDark,
                cursorColor = ColorGender,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedSuffixColor = ColorGenderDark,
                unfocusedSuffixColor = ColorGender,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            trailingIcon = { content() },
            readOnly = !isEditable
        )
    }
}

@Composable
fun DialogConfirm(
    title: String = "",
    text: String = "",
    icon: ImageVector = Icons.Rounded.Info,
    confirmText: String = stringResource(id = R.string.confirm),
    dismissText: String = stringResource(id = R.string.cancel),
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = icon, contentDescription = null, tint = ColorGenderDark)
                Spacer(Modifier.height(16.dp))
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Text(text = text, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Justify)
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(dismissText, color = ColorGenderDark)
                    }
                    Spacer(Modifier.width(16.dp))
                    TextButton(onClick = { onConfirm() }) {
                        Text(confirmText, color = ColorGenderDark)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK", color = ColorGenderDark)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = ColorGenderDark)
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}