package com.firmino.hinedigital.view.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.BallsAnim
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderDarker
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.ThemeGender
import com.firmino.hinedigital.view.theme.fontTulpen
import com.firmino.hinedigital.view.theme.fontZenDots
import com.firmino.hinedigital.view.theme.getColorTheme
import com.firmino.hinedigital.view.theme.toggleTheme
import java.util.Locale

@Composable
fun MenuScreen(navController: NavController) {
    val context = LocalContext.current
    var theme by remember { mutableStateOf(getColorTheme(context) == ThemeGender.FEMALE) }
    var dialogDeveloperVisible by remember { mutableStateOf(false) }
    var dialogLicencesVisible by remember { mutableStateOf(false) }

    if (dialogDeveloperVisible) DialogDeveloperNotes(onDismiss = { dialogDeveloperVisible = false }) {
        context.startActivity(Intent(Intent.ACTION_VIEW, "https://github.com/firminoveras".toUri()))
    }
    if (dialogLicencesVisible) DialogLicencesNotes(onDismiss = { dialogLicencesVisible = false })

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        key(theme) {
            Box(Modifier.padding(innerPadding).fillMaxSize().background(Brush.linearGradient(listOf(ColorGender, ColorGenderDark)))) {
                Column {
                    BallsAnim(Modifier.weight(1f))
                    Column(Modifier.weight(2f)) {
                        Logo(context)
                        Spacer(modifier = Modifier.height(18.dp))
                        Menu(navController)
                    }
                }
                Column(Modifier.padding(12.dp).align(Alignment.BottomEnd), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                        onClick = { dialogDeveloperVisible = true }) {
                        Icon(ImageVector.vectorResource(id = R.drawable.ic_info), contentDescription = null, tint = ColorGenderDark)
                    }
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                        onClick = { dialogLicencesVisible = true }) {
                        Icon(ImageVector.vectorResource(id = R.drawable.ic_policy), contentDescription = null, tint = ColorGenderDark)
                    }
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                        onClick = { theme = toggleTheme(context) }) {
                        Icon(ImageVector.vectorResource(id = R.drawable.ic_theme), contentDescription = null, tint = ColorGenderDark)
                    }
                }
            }
        }
    }
}

@Composable
fun Menu(navController: NavController) {
    ButtonPrimary(label = "Avaliações", icon = Icons.AutoMirrored.Rounded.ArrowForward) {
        navController.navigate("evaluationList")
    }
    ButtonSecondary(label = "Guia Hine") {
        navController.navigate("guide")
    }
    ButtonSecondary(label = "Informações") {
        navController.navigate("information")
    }
    ButtonSecondary(label = "Política de Privacidade") {
        navController.navigate("policy")
    }
}

@Composable
fun Logo(context: Context) {
    Box(Modifier.padding(start = 32.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = "HINE", fontFamily = fontZenDots, color = Color.White, fontSize = 80.sp)
            Text(
                modifier = Modifier.offset(y = (-12).dp),
                text = "Digital",
                fontFamily = fontTulpen,
                color = ColorGenderLight,
                fontSize = 40.sp,
            )
        }
        Text(
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 6.dp),
            text = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "Versão de Teste",
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
private fun ButtonSecondary(label: String, onClick: () -> Unit = {}) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
        onClick = { onClick() }
    ) {
        Box(Modifier.clickable { onClick() }) {
            Spacer(
                modifier = Modifier
                    .width(8.dp)
                    .height(38.dp)
                    .background(color = Color.White, RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                    .align(Alignment.CenterStart)
            )
            Text(
                modifier = Modifier.padding(horizontal = 38.dp).align(Alignment.CenterStart),
                text = label.uppercase(Locale.getDefault()),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ButtonPrimary(@Suppress("SameParameterValue") label: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
        onClick = { onClick() }
    ) {
        Row(Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = label.uppercase(Locale.getDefault()),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ColorGenderDark,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                modifier = Modifier.background(ColorGenderDark, CircleShape).size(32.dp),
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun DialogLicencesNotes(onDismiss: () -> Unit = {}) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(24.dp)) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_policy), contentDescription = null, tint = ColorGenderDark, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(16.dp))
                Text(text = "Licenças e Bibliotecas", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(16.dp))
                Text(text = "Apache PDFBox® - A Java PDF Library", style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Text(text = "Apache PDFBox is published under the Apache License v2.0.", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(8.dp))
                Text(text = "Material Design Icons", style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Text(text = "Material Design Icons is published under the Apache License v2.0.", style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Justify, color = ColorGenderDarker)
                Spacer(Modifier.height(16.dp))
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
fun DialogDeveloperNotes(onDismiss: () -> Unit = {}, onClick: () -> Unit = {}) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(24.dp)) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_info), contentDescription = null, tint = ColorGenderDark, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(16.dp))
                Text(text = "Desenvolvedores", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(16.dp))
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
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Fechar", color = ColorGenderDark)
                    }
                }
            }
        }
    }
}