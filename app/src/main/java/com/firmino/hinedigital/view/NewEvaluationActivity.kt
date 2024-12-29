package com.firmino.hinedigital.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.firmino.hinedigital.HINEApplication
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.Header
import com.firmino.hinedigital.extensions.toBrazilianDateFormat
import com.firmino.hinedigital.model.entity.Evaluation
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.view.theme.ThemeGender
import com.firmino.hinedigital.view.theme.getColorTheme
import com.firmino.hinedigital.view.theme.setColorTheme
import com.firmino.hinedigital.view.views.CreateConfirmDialog
import com.firmino.hinedigital.view.views.DatePickerModal
import com.firmino.hinedigital.viewmodel.EvaluationViewModel
import com.firmino.hinedigital.viewmodel.factory.EvaluationModelViewFactory
import kotlinx.coroutines.launch
import java.util.Locale

class NewEvaluationActivity : ComponentActivity() {
    private val viewModel: EvaluationViewModel by viewModels { EvaluationModelViewFactory((application as HINEApplication).repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HINEDigitalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var genderTheme by remember { mutableStateOf(true) }
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Brush.linearGradient(listOf(ColorGender, ColorGenderDark)))
                    ) {
                        Header(title = "Nova Avaliação", onBackPressed = {
                            startActivity(Intent(this@NewEvaluationActivity, MenuActivity::class.java))
                            finish()
                        })
                        Content(genderTheme = genderTheme) { genderTheme = it }
                    }
                }
            }
        }
    }

    @Composable
    private fun Content(genderTheme: Boolean, onThemeChange: (newTheme: Boolean) -> Unit = {}) {
        var name by remember { mutableStateOf("") }
        var gender by remember { mutableStateOf(if (getColorTheme(this@NewEvaluationActivity) == ThemeGender.FEMALE) "feminino" else "masculino") }
        var birthday by remember { mutableStateOf("") }
        var gestationalWeeks by remember { mutableStateOf("") }
        var correctedAge by remember { mutableStateOf("") }
        var cephalicSize by remember { mutableStateOf("") }
        var create by remember { mutableStateOf(false) }
        var created by remember { mutableStateOf(false) }
        var showDatePickerDialog by remember { mutableStateOf(false) }

        key(genderTheme) {
            if (showDatePickerDialog) {
                DatePickerModal(
                    onDateSelected = { if (it != null) birthday = it.toBrazilianDateFormat() },
                    onDismiss = { showDatePickerDialog = false }
                )
            }

            if (create) {
                CreateConfirmDialog(
                    onConfirm = {
                        viewModel.insert(
                            Evaluation(
                                name = name,
                                gender = gender,
                                birthday = birthday,
                                gestationalWeeks = gestationalWeeks.toInt(),
                                correctedAge = correctedAge.toInt(),
                                cephalicSize = cephalicSize.toInt()
                            )
                        )
                        created = true
                        create = false
                    },
                    onDismiss = {
                        create = false
                    }
                )
            }

            if (created) DialogEvaluationCreate()

            val pageState = rememberPagerState(initialPage = 0, pageCount = { 5 })
            val rememberCoroutine = rememberCoroutineScope()

            Box {
                HorizontalPager(state = pageState) { page ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        when (page) {
                            0 -> {
                                Text(stringResource(R.string.name), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                                TextInput(value = name, onUpdate = { name = it }) {
                                    rememberCoroutine.launch { pageState.animateScrollToPage(1) }
                                }
                                GenderSelector(gender) {
                                    gender = it
                                    setColorTheme(if (gender == "feminino") ThemeGender.FEMALE else ThemeGender.MALE, this@NewEvaluationActivity)
                                    onThemeChange(!genderTheme)
                                }
                            }

                            1 -> {
                                Text(stringResource(R.string.born_date), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                                TextInput(modifier = Modifier.pointerInput(birthday) {
                                    awaitEachGesture {
                                        awaitFirstDown(pass = PointerEventPass.Initial)
                                        if (waitForUpOrCancellation(pass = PointerEventPass.Initial) != null) showDatePickerDialog = true
                                    }
                                }, value = birthday, hint = stringResource(R.string.hint_birthdate), isEditable = false, maxLength = 10, trailingIcon = { Icon(Icons.Rounded.DateRange, contentDescription = null, tint = ColorGenderDark) }) {
                                    rememberCoroutine.launch { pageState.animateScrollToPage(2) }
                                }
                            }

                            2 -> {
                                Text(stringResource(R.string.gestational_year), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                                TextInput(
                                    keyboardType = KeyboardType.Number,
                                    value = gestationalWeeks,
                                    maxLength = 4,
                                    suffix = stringResource(R.string.weeks),
                                    onUpdate = { gestationalWeeks = it }
                                ) {
                                    rememberCoroutine.launch { pageState.animateScrollToPage(3) }
                                }
                                InfoText(title = stringResource(R.string.gestational_year), info = stringResource(R.string.gestational_year_info))
                            }

                            3 -> {
                                Text(stringResource(R.string.corrected_age), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                                TextInput(
                                    keyboardType = KeyboardType.Number,
                                    value = correctedAge,
                                    maxLength = 4,
                                    suffix = stringResource(R.string.weeks),
                                    onUpdate = { correctedAge = it }
                                ) {
                                    rememberCoroutine.launch { pageState.animateScrollToPage(4) }
                                }
                                InfoText(title = stringResource(R.string.corrected_age), info = stringResource(R.string.corrected_age_info))
                            }

                            4 -> {
                                Text(stringResource(R.string.cephalic_size), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                                TextInput(
                                    keyboardType = KeyboardType.Number,
                                    value = cephalicSize,
                                    maxLength = 4,
                                    suffix = stringResource(R.string.centimeter),
                                    onUpdate = { cephalicSize = it }
                                )
                                InfoText(title = stringResource(R.string.cephalic_size), info = stringResource(R.string.cephalic_size_info))
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (name.isNotBlank() && birthday.isNotBlank() && gestationalWeeks.isNotBlank() && correctedAge.isNotBlank() && cephalicSize.isNotBlank()) {
                        DoneButton(label = stringResource(R.string.done_and_start), icon = Icons.AutoMirrored.Rounded.ArrowForward) {
                            create = true
                        }
                    }
                    PageNavigation(listOf(name.isNotBlank(), birthday.isNotBlank(), gestationalWeeks.isNotBlank(), correctedAge.isNotBlank(), cephalicSize.isNotBlank()), state = pageState) { rememberCoroutine.launch { pageState.animateScrollToPage(it) } }
                }
            }
        }
    }

    @Composable
    fun PageNavigation(pageStatus: List<Boolean>, state: PagerState, onNavigate: (newPage: Int) -> Unit) {

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
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
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                repeat(state.pageCount) { page ->
                    Icon(
                        imageVector = ImageVector.vectorResource(id = if (pageStatus[page]) R.drawable.ic_done_on else R.drawable.ic_done_off),
                        contentDescription = null,
                        tint = if (page == state.currentPage) ColorGenderLight else ColorGenderDarker,
                        modifier = Modifier.size(if (page == state.currentPage) 20.dp else 12.dp)
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

    @Composable
    private fun InfoText(title: String, info: String) {
        var extended by remember { mutableStateOf(false) }
        Column(modifier = Modifier.alpha(if (extended) 1f else 0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(onClick = { extended = !extended }, shape = RoundedCornerShape(32.dp), color = Color.Transparent) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Rounded.Info, contentDescription = null, tint = Color.White)
                    Text("O que é $title?", color = Color.White, style = MaterialTheme.typography.labelLarge)
                }
            }
            AnimatedVisibility(visible = extended) {
                Text(modifier = Modifier.padding(horizontal = 18.dp), textAlign = TextAlign.Center, text = info, color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }

    @Composable
    private fun GenderSelector(gender: String, onGenderUpdate: (gender: String) -> Unit) {
        Row {
            Surface(
                onClick = { onGenderUpdate("masculino") },
                shape = RoundedCornerShape(topStart = 32.dp, bottomStart = 32.dp),
                color = if (gender == "masculino") Color.White else Color.Transparent,
                border = if (gender == "masculino") null else ButtonDefaults.outlinedButtonBorder().copy(width = 2.dp, brush = Brush.linearGradient(listOf(Color.White, Color.White)))
            ) {
                Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(ImageVector.vectorResource(id = R.drawable.ic_male), contentDescription = null, tint = if (gender == "masculino") ColorGenderDark else Color.White)
                    Text("Masculino", color = if (gender == "masculino") ColorGenderDark else Color.White)
                }
            }
            Surface(
                onClick = { onGenderUpdate("feminino") },
                shape = RoundedCornerShape(bottomEnd = 32.dp, topEnd = 32.dp),
                color = if (gender == "feminino") Color.White else Color.Transparent,
                border = if (gender == "feminino") null else ButtonDefaults.outlinedButtonBorder().copy(width = 2.dp, brush = Brush.linearGradient(listOf(Color.White, Color.White)))
            ) {
                Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(ImageVector.vectorResource(id = R.drawable.ic_female), contentDescription = null, tint = if (gender == "feminino") ColorGenderDark else Color.White)
                    Text("Feminino", color = if (gender == "feminino") ColorGenderDark else Color.White)
                }
            }
        }
    }

    @Composable
    private fun DialogEvaluationCreate() {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    startActivity(Intent(this@NewEvaluationActivity, ListEvaluationActivity::class.java))
                    finish()
                }) {
                    Text(text = "Confirmar")
                }
            },
            title = { Text(text = "Sucesso") },
            text = { Text(text = "Avaliaçao criada com sucesso.") },
            icon = { Icon(Icons.Rounded.Check, contentDescription = null) })
    }

    @Composable
    private fun DoneButton(
        modifier: Modifier = Modifier,
        label: String,
        icon: ImageVector,
        onClick: () -> Unit = {}
    ) {
        Surface(
            modifier = modifier,
            onClick = { onClick() },
            color = Color.White,
            shape = RoundedCornerShape(32.dp)
        ) {
            Row(Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label.uppercase(Locale.getDefault()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    color = ColorGenderDark,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .background(ColorGenderDark, CircleShape)
                        .size(32.dp)
                        .padding(4.dp)
                )
            }
        }
    }

    @Composable
    private fun TextInput(
        modifier: Modifier = Modifier,
        keyboardType: KeyboardType = KeyboardType.Text,
        maxLength: Int = 256,
        value: String = "",
        onUpdate: (value: String) -> Unit = {},
        isEditable: Boolean = true,
        suffix: String = "",
        hint: String = "",
        trailingIcon: @Composable () -> Unit = {},
        onDone: () -> Unit = {}
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        TextField(
            modifier = modifier.fillMaxWidth(),
            readOnly = !isEditable,
            value = value,
            placeholder = { Text(text = hint, color = ColorGender) },
            singleLine = true,
            suffix = { Text(suffix) },
            trailingIcon = trailingIcon,
            onValueChange = { onUpdate(it.substring(0, kotlin.math.min(it.length, maxLength))) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done,
                keyboardType = keyboardType,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onDone()
                }
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
            shape = RoundedCornerShape(32.dp)
        )
    }

}