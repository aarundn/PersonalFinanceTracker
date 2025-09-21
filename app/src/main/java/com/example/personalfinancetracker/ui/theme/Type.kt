package com.example.personalfinancetracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    displayLarge = TextStyle( // H1
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 36.sp
    ),
    headlineMedium = TextStyle( // H2
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 30.sp
    ),
    headlineSmall = TextStyle( // H3
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 27.sp
    ),
    titleLarge = TextStyle( // H4
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle( // Paragraph
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    labelLarge = TextStyle( // Labels, Buttons
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle( // Inputs
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    )
)
