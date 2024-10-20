package com.example.aiapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.aiapp.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val universoFontFamily = FontFamily(
    Font(R.font.universo_regular, FontWeight.Normal), // Define the font with its weight
    Font(R.font.universo_bold, FontWeight.Bold), // Define the font with its weight
    Font(R.font.universo_light, FontWeight.Light), // Define the font with its weight
    Font(R.font.universo_black, FontWeight.Black) ,// Define the font with its weight
    Font(R.font.universo_thin, FontWeight.Thin) // Define the font with its weight
     // Define the font with its weight
)