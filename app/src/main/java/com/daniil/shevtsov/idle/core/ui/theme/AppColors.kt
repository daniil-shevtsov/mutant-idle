package com.daniil.shevtsov.idle.core.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors(
    backgroundLight: Color,
    background: Color,
    backgroundDark: Color,
    backgroundDarkest: Color,
    backgroundText: Color,
    textDark: Color,
    textLight: Color,
    iconLight: Color,
) {
    var backgroundLight by mutableStateOf(backgroundLight)
        private set
    var background by mutableStateOf(background)
        private set
    var backgroundDark by mutableStateOf(backgroundDark)
        private set
    var backgroundDarkest by mutableStateOf(backgroundDarkest)
        private set
    var backgroundText by mutableStateOf(backgroundText)
        private set
    var textDark by mutableStateOf(textDark)
        private set
    var textLight by mutableStateOf(textLight)
        private set
    var iconLight by mutableStateOf(iconLight)
        private set

    fun copy(
        backgroundLight: Color = this.backgroundLight,
        background: Color = this.background,
        backgroundDark: Color = this.backgroundDark,
        backgroundDarkest: Color = this.backgroundDarkest,
        backgroundText: Color = this.backgroundText,
        textDark: Color = this.textDark,
        textLight: Color = this.textLight,
        iconLight: Color = this.iconLight,
    ): AppColors = AppColors(
        backgroundLight = backgroundLight,
        background = background,
        backgroundDark = backgroundDark,
        backgroundDarkest = backgroundDarkest,
        backgroundText = backgroundText,
        textDark = textDark,
        textLight = textLight,
        iconLight = iconLight,
    )

    fun updateColorsFrom(other: AppColors) {
        backgroundLight = other.backgroundLight
        background = other.background
        backgroundDark = other.backgroundDark
        backgroundDarkest = other.backgroundDarkest
        backgroundText = other.backgroundText
        textDark = other.textDark
        textLight = other.textLight
        iconLight = other.iconLight
    }
}

fun defaultColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFD64747),
    background = Color(0xFFAF0A0A),
    backgroundDark = Color(0xFF750404),
    backgroundDarkest = Color(0xFF2F1B1B),
    backgroundText = Color.White,
    textDark = Color.Black,
    textLight = Color.White,
    iconLight = Color.White,
)

fun testColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFFF0000),
    background = Color(0xFFFF0000),
    backgroundDark = Color(0xFFFF0000),
    backgroundDarkest = Color(0xFFFF0000),
    backgroundText = Color(0xFFFF0000),
    textDark = Color(0xFFFF0000),
    textLight = Color(0xFFFF0000),
    iconLight = Color(0xFFFF0000),
)

internal val LocalColors = staticCompositionLocalOf { defaultColors() }
