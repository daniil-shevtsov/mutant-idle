package com.daniil.shevtsov.idle.feature.colors.presentation

import androidx.compose.ui.graphics.Color
import com.daniil.shevtsov.idle.core.ui.theme.AppColors

data class SpeciesColors(
    val backgroundLight: DomainColor,
    val background: DomainColor,
    val backgroundDark: DomainColor,
    val backgroundDarkest: DomainColor,
    val backgroundText: DomainColor,
    val textDark: DomainColor,
    val textLight: DomainColor,
    val iconLight: DomainColor,
)

fun SpeciesColors.toAppColors(): AppColors = AppColors(
    backgroundLight = backgroundLight.toCompose(),
    background = background.toCompose(),
    backgroundDark = backgroundDark.toCompose(),
    backgroundDarkest = backgroundDarkest.toCompose(),
    backgroundText = backgroundText.toCompose(),
    textDark = textDark.toCompose(),
    textLight = textLight.toCompose(),
    iconLight = iconLight.toCompose(),
)

fun speciesColors(
    backgroundLight: DomainColor = DomainColor(0xFFAF0A0A),
    background: DomainColor = DomainColor(0xFFAF0A0A),
    backgroundDark: DomainColor = DomainColor(0xFFAF0A0A),
    backgroundDarkest: DomainColor = DomainColor(0xFFAF0A0A),
    backgroundText: DomainColor = DomainColor(0xFFAF0A0A),
    textDark: DomainColor = DomainColor(0xFFAF0A0A),
    textLight: DomainColor = DomainColor(0xFFAF0A0A),
    iconLight: DomainColor = DomainColor(0xFFAF0A0A),
) = SpeciesColors(
    backgroundLight = backgroundLight,
    background = background,
    backgroundDark = backgroundDark,
    backgroundDarkest = backgroundDarkest,
    backgroundText = backgroundText,
    textDark = textDark,
    textLight = textLight,
    iconLight = iconLight
)

data class DomainColor(val argb: Long)

fun DomainColor.toCompose(): Color = Color(
    color = argb
)
