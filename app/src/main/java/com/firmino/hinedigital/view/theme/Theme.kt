package com.firmino.hinedigital.view.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.firmino.hinedigital.R

private val DarkColorScheme = darkColorScheme(
    primary = ColorGenderDark, secondary = ColorGender, tertiary = ColorGenderLight
)

private val LightColorScheme = lightColorScheme(
    primary = ColorGenderDark, secondary = ColorGender, tertiary = ColorGenderDarker
)

val fontZenDots = FontFamily(Font(R.font.zendots, FontWeight.Normal))
val fontTulpen = FontFamily(Font(R.font.tulpen, FontWeight.Normal))
val mplus = FontFamily(Font(R.font.mplus))

@Composable
fun HINEDigitalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(colorScheme = LightColorScheme, typography = Typography, content = content)
}