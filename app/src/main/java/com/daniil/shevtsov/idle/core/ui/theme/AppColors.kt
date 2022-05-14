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

fun devourerColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFD64747),
    background = Color(0xFFAF0A0A),
    backgroundDark = Color(0xFF750404),
    backgroundDarkest = Color(0xFF2F1B1B),
    backgroundText = Color.White,
    textDark = Color.Black,
    textLight = Color.White,
    iconLight = Color.White,
)

fun alienColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFCDDEE5),
    background = Color(0xFF94ACB6),
    backgroundDark = Color(0xFF5A6E76),
    backgroundDarkest = Color(0xFF36454B),
    backgroundText = Color.White,
    textDark = Color(0xFF26235A),
    textLight = Color(0xFFE0DEFB),
    iconLight = Color(0xFFE0DEFB),
)

internal val LocalColors = staticCompositionLocalOf { devourerColors() }
