package com.example.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.core.R

val interFamily = FontFamily(
    Font(R.font.inter_thin, FontWeight.Thin),
    Font(R.font.inter_extra_light, FontWeight.ExtraLight),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extra_bold, FontWeight.ExtraBold),
    Font(R.font.inter_black, FontWeight.Black)
)
val ibmPlexSansArabic = FontFamily(
    Font(R.font.ibm_plex_sans_arabic_bold, FontWeight.Bold),
    Font(R.font.ibm_plex_sans_arabic_regular, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_arabic_light, FontWeight.Light),
    Font(R.font.ibm_plex_sans_arabic_medium, FontWeight.Medium),

)


val AppTypography = Typography(
    // Display Styles (Very Large Headlines)
    displayLarge = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    ),

    // Headline Styles (Important Titles)
    headlineLarge = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title Styles (Section titles)
    titleLarge = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body Styles (Normal text)
    bodyLarge = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Label Styles (Buttons, Chips, Small elements)
    labelLarge = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    )
)







