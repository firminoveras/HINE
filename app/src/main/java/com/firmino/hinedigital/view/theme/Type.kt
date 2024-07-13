package com.firmino.hinedigital.view.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.firmino.hinedigital.R

object AppFont {
    val mPlusFont = FontFamily(
        Font(R.font.mplus_regular),
        Font(R.font.mplus_medium, FontWeight.Medium),
        Font(R.font.mplus_bold, FontWeight.Bold),
        Font(R.font.mplus_black, FontWeight.Black),
        Font(R.font.mplus_extrabold, FontWeight.ExtraBold),
        Font(R.font.mplus_light, FontWeight.ExtraLight),
        Font(R.font.mplus_thin, FontWeight.Thin),
    )
}
private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFont.mPlusFont),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFont.mPlusFont),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFont.mPlusFont),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont.mPlusFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont.mPlusFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont.mPlusFont),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont.mPlusFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont.mPlusFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont.mPlusFont),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont.mPlusFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont.mPlusFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont.mPlusFont),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont.mPlusFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont.mPlusFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont.mPlusFont)
)
