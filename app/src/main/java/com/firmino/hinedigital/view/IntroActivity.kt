package com.firmino.hinedigital.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmino.hinedigital.R
import com.firmino.hinedigital.view.theme.ColorGender
import com.firmino.hinedigital.view.theme.ColorGenderDark
import com.firmino.hinedigital.view.theme.ColorGenderLight
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.view.theme.fontTulpen
import com.firmino.hinedigital.view.theme.fontZenDots
import com.firmino.hinedigital.view.theme.refreshTheme
import kotlinx.coroutines.delay

class IntroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            refreshTheme(this@IntroActivity)
            HINEDigitalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var progressValue by remember { mutableFloatStateOf(0f) }
                    LaunchedEffect(true) {
                        while (progressValue <= 1f) {
                            delay(10)
                            progressValue += 0.005f
                        }
                        if(progressValue >= 1f){
                            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(180.dp),
                                painter = painterResource(id = R.drawable.bg_logo),
                                contentDescription = "Logo"
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Spacer(modifier = Modifier.width(120.dp))
                                Text(text = "HINE", fontFamily = fontZenDots, color = ColorGenderDark, fontSize = 42.sp)
                                Text(
                                    text = "Digital",
                                    fontFamily = fontTulpen,
                                    color = ColorGender,
                                    fontSize = 32.sp,
                                    modifier = Modifier.width(120.dp)
                                )
                            }
                            Text(text = "CONEX AÇÃO", color = ColorGenderDark)
                            Spacer(modifier = Modifier.height(52.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.size(42.dp).rotate(progressValue * 360),
                                progress = { progressValue },
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 12.dp,
                                trackColor = ColorGenderLight
                            )
                            Spacer(modifier = Modifier.height(52.dp))
                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.primary)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = packageManager.getPackageInfo(packageName, 0).versionName ?: "Versão de Teste",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White
                            )
                            Text(
                                text = "Desenvolvido por Firmino Veras",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Text(text = "2024", style = MaterialTheme.typography.bodySmall, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}