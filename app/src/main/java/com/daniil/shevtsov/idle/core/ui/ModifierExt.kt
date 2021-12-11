package com.daniil.shevtsov.idle.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal fun Modifier.innerBorder(
    lightColor: Color,
    darkColor: Color,
    size: Dp = 1.dp,
) = background(darkColor)
    .padding(top = size, start = size)
    .background(lightColor)
    .padding(bottom = size, end = size)
