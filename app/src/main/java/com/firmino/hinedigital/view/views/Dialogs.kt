package com.firmino.hinedigital.view.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.toBrazilianDateFormat
import com.firmino.hinedigital.model.entity.Evaluation
import com.firmino.hinedigital.view.theme.ColorBlueDark
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.ColorPinkDark
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
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
                EditTextField(value = name, onValueChange = { name = it }, label = LocalContext.current.getString(R.string.name), keyboardType = KeyboardType.Text) {
                    Icon(
                        ImageVector.vectorResource(id = if (gender == "feminino") R.drawable.ic_female else R.drawable.ic_male), contentDescription = null,
                        tint = if (gender == "feminino") ColorPinkDark else ColorBlueDark, modifier = Modifier.clickable {
                            gender = if (gender == "feminino") "masculino" else "feminino"
                        })
                }
                EditTextField(value = birthday, onValueChange = { birthday = it }, label = LocalContext.current.getString(R.string.born_date), maxLength = 10, isEditable = false) {
                    Icon(Icons.Rounded.DateRange, contentDescription = null, tint = ColorGenderDark, modifier = Modifier.clickable {
                        showDatePickerDialog = true
                    })
                }
                EditTextField(value = gestationalWeeks, onValueChange = { gestationalWeeks = it }, label = LocalContext.current.getString(R.string.gestational_year), maxLength = 4) {
                    Text(text = "Semanas", color = ColorGenderDark, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(end = 8.dp))
                }
                EditTextField(value = correctedAge, onValueChange = { correctedAge = it }, label = LocalContext.current.getString(R.string.corrected_age), maxLength = 4) {
                    Text(text = "Semanas", color = ColorGenderDark, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(end = 8.dp))
                }
                EditTextField(value = cephalicSize, onValueChange = { cephalicSize = it }, label = LocalContext.current.getString(R.string.cephalic_size), maxLength = 4) {
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
private fun EditTextField(value: String, isEditable: Boolean = true, onValueChange: (newValue: String) -> Unit, label: String, maxLength: Int = 256, keyboardType: KeyboardType = KeyboardType.Number, content: @Composable () -> Unit = {}) {
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

@Composable
fun DialogDevNotes(onDismiss: () -> Unit = {}, onClick: () -> Unit = {}) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(24.dp)) {
                Icon(modifier = Modifier.fillMaxWidth(), imageVector = ImageVector.vectorResource(R.drawable.ic_dev_comment), contentDescription = null, tint = ColorGenderDark)
                Spacer(Modifier.height(16.dp))
                Text(modifier = Modifier.fillMaxWidth(), text = "Notas do Desenvolvedor", style = MaterialTheme.typography.titleLarge, color = ColorGenderDarker)
                Spacer(Modifier.height(16.dp))
                Text(text = "Licenças e Bibliotecas", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(8.dp))
                Text(text = "Apache PDFBox® - A Java PDF Library", style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Text(text = "Apache PDFBox is published under the Apache License v2.0.", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(8.dp))
                Text(text = "Material Design Icons", style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Text(text = "Material Design Icons is published under the Apache License v2.0.", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(16.dp))
                Text(text = "Desenvolvedores", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(8.dp))
                Text(text = "Halbiege Léa Di Pace Quirino Da Silva", style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Text(text = "Mestranda", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(8.dp))
                Text(text = "Prof.ª Dra. Maria Denise Leite Ferreira", style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Text(text = "Orientadora", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(8.dp))
                Text(text = "Prof.ª Dra. Renata Ramos Tomaz", style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Text(text = "Coorientadora", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(8.dp))
                Text(text = "José Firmino Veras Neto", style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Text(text = "Desenvolvedor e Designer", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(16.dp))
                Button(modifier = Modifier.fillMaxWidth(), onClick = { onClick() }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_github), contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(24.dp))
                    Text("Página do Desenvolvedor")
                }
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Fechar", color = ColorGenderDark)
                    }
                }
            }
        }
    }
}

@Composable
fun DialogTutorial(
    title: String = "",
    tutorial: String = "",
    onDismiss: () -> Unit = {},
) {
    var index by remember { mutableIntStateOf(0) }
    val textParagraphs = tutorial.split(". ")
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Rounded.Info, contentDescription = null, tint = ColorGenderDark)
                Spacer(Modifier.height(16.dp))
                Text(text = title, style = MaterialTheme.typography.titleLarge, color = ColorGenderDarker)
                Spacer(Modifier.height(8.dp))
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
                Spacer(Modifier.height(16.dp))
                Text(text = textParagraphs[index] + ".", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Fechar", color = ColorGenderDark)
                    }
                }
            }
        }
    }
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
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun CreateConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {onConfirm()}) {
                Text(text = "Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        title = { Text(text = stringResource(R.string.new_evaluation_title)) },
        text = { Text(text = stringResource(R.string.new_evaluation_text)) },
        icon = { Icon(Icons.Rounded.CheckCircle, contentDescription = null) })
}