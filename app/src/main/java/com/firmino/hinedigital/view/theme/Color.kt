package com.firmino.hinedigital.view.theme

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit

enum class ThemeGender { FEMALE, MALE }

val ColorPinkDarker = Color(0xFF7D5260)
val ColorPinkDark = Color(0xFFC594AA)
val ColorPink = Color(0xFFFDCAE1)
val ColorPinkLight = Color(0xFFFFE6F1)

val ColorBlueDarker = Color(0xFF25736D)
val ColorBlueDark = Color(0xFF5DC1B9)
val ColorBlue = Color(0xFF9CE0DB)
val ColorBlueLight = Color(0xFFD7FAF8)

var ColorGenderDarker = Color(0xFF7D5260)
var ColorGenderDark = Color(0xFFC594AA)
var ColorGender = Color(0xFFFDCAE1)
var ColorGenderLight = Color(0xFFFFE6F1)

fun getColorTheme(context: Context): ThemeGender {
    return if(context.getSharedPreferences("com.firmino.hinedigital", Context.MODE_PRIVATE).getBoolean("theme", false)){
        ThemeGender.FEMALE
    } else{
        ThemeGender.MALE
    }
}

fun refreshTheme(context: Context){
    setColorTheme(getColorTheme(context), context)
}

fun toggleTheme(context: Context): Boolean{
    setColorTheme(if(getColorTheme(context) == ThemeGender.FEMALE) ThemeGender.MALE else ThemeGender.FEMALE, context)
    return getColorTheme(context) == ThemeGender.FEMALE
}

fun setColorTheme(themeGender: ThemeGender, context: Context) {
    context.getSharedPreferences("com.firmino.hinedigital", Context.MODE_PRIVATE).edit { putBoolean("theme", themeGender == ThemeGender.FEMALE) }
    when (themeGender) {
        ThemeGender.FEMALE -> {
            ColorGenderDarker = ColorPinkDarker
            ColorGenderDark = ColorPinkDark
            ColorGender = ColorPink
            ColorGenderLight = ColorPinkLight
        }

        ThemeGender.MALE -> {
            ColorGenderDarker = ColorBlueDarker
            ColorGenderDark = ColorBlueDark
            ColorGender = ColorBlue
            ColorGenderLight = ColorBlueLight
        }
    }
}