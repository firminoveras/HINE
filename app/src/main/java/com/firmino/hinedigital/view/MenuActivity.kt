package com.firmino.hinedigital.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmino.hinedigital.R
import com.firmino.hinedigital.extensions.BallsAnim
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.view.theme.ThemeGender
import com.firmino.hinedigital.view.theme.fontTulpen
import com.firmino.hinedigital.view.theme.fontZenDots
import com.firmino.hinedigital.view.theme.getColorTheme
import com.firmino.hinedigital.view.theme.toggleTheme
import com.firmino.hinedigital.view.views.DialogDevNotes
import java.util.Locale

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var theme by remember { mutableStateOf(getColorTheme(this@MenuActivity) == ThemeGender.FEMALE) }
            HINEDigitalTheme {

                var infoDialogVisible by remember { mutableStateOf(false) }
                if (infoDialogVisible) DialogDevNotes(onDismiss = { infoDialogVisible = false }) {
                    val url = "https://github.com/firminoveras"
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    key(theme) {
                        Box(
                            Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(Brush.linearGradient(listOf(ColorGender, ColorGenderDark)))
                        ) {
                            Column {
                                BallsAnim(Modifier.weight(1f))
                                Column(Modifier.weight(2f)) {
                                    Logo()
                                    Spacer(modifier = Modifier.height(18.dp))
                                    Menu()

                                    Column(
                                        Modifier
                                            .fillMaxSize()
                                            .padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom), horizontalAlignment = Alignment.End
                                    ) {
                                        IconButton(
                                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                                            onClick = { infoDialogVisible = true }) {
                                            Icon(ImageVector.vectorResource(id = R.drawable.ic_dev_comment), contentDescription = null, tint = ColorGenderDark)
                                        }
                                        IconButton(
                                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                                            onClick = { theme = toggleTheme(this@MenuActivity) }) {
                                            Icon(ImageVector.vectorResource(id = R.drawable.ic_theme), contentDescription = null, tint = ColorGenderDark)
                                        }
                                    }
                                }
                            }

                            Text(
                                text = packageManager.getPackageInfo(packageName, 0).versionName ?: "Versão de Teste", fontSize = 12.sp, color = Color.White, modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun Menu() {
        ButtonPrimary(label = "Nova Avaliação", icon = Icons.AutoMirrored.Rounded.ArrowForward) {
            startActivity(Intent(this@MenuActivity, NewEvaluationActivity::class.java))
            finish()
        }
        ButtonSecondary(label = "Últimas Avaliações") {
            startActivity(Intent(this@MenuActivity, ListEvaluationActivity::class.java))
            finish()
        }
        ButtonSecondary(label = "Guia Hine") {
            startActivity(Intent(this@MenuActivity, GuideActivity::class.java))
            finish()
        }
        ButtonSecondary(label = "Informações") {
            startActivity(Intent(this@MenuActivity, InformationActivity::class.java))
            finish()
        }
    }

    @Composable
    fun Logo() {
        Column {
            Row(Modifier.padding(start = 32.dp), verticalAlignment = Alignment.Bottom) {
                Text(text = "HINE", fontFamily = fontZenDots, color = Color.White, fontSize = 80.sp)
                Text(
                    modifier = Modifier.offset(y = (-12).dp),
                    text = "Digital",
                    fontFamily = fontTulpen,
                    color = ColorGenderLight,
                    fontSize = 40.sp,
                )
            }
        }

    }

    @Composable
    private fun ButtonSecondary(label: String, onClick: () -> Unit = {}) {
        Surface(
            color = Color.Transparent,
            shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
            onClick = { onClick() }
        ) {
            Box(
                Modifier
                    .clickable { onClick() }) {
                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                        .height(38.dp)
                        .background(
                            color = Color.White, RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                        )
                        .align(Alignment.CenterStart)
                )
                Text(
                    text = label.uppercase(Locale.getDefault()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(horizontal = 38.dp)
                        .align(Alignment.CenterStart),
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
                )
            }


        }
    }
}