package com.daniil.shevtsov.idle.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val rubik = FontFamily.Default
private val openSans = FontFamily.Monospace

data class AppTypography(
    val h1: TextStyle = TextStyle(
        fontFamily = rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    val subtitle: TextStyle = TextStyle(
        fontFamily = openSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val body: TextStyle = TextStyle(
        fontFamily = openSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val button: TextStyle = TextStyle(
        fontFamily = rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = openSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
