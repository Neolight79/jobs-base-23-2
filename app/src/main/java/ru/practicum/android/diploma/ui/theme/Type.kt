package ru.practicum.android.diploma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ru.practicum.android.diploma.R

val DiplomaFontFamily = FontFamily(
    Font(R.font.ys_display_bold, FontWeight.Bold),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_regular, FontWeight.Normal)
)


val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = DiplomaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSizeRegular,
        lineHeight = LineHeightRegular,
        letterSpacing = LetterSpacing
    ),
    titleLarge = TextStyle(
        fontFamily = DiplomaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSizeLarge,
        lineHeight = LineHeightLarge,
        letterSpacing = LetterSpacing
    ),
    titleMedium = TextStyle(
        fontFamily = DiplomaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSizeMedium,
        lineHeight = LineHeightMedium,
        letterSpacing = LetterSpacing
    ),
    titleSmall = TextStyle(
        fontFamily = DiplomaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSizeSmall,
        lineHeight = LineHeightSmall,
        letterSpacing = LetterSpacing
    )

)
