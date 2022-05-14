package com.daniil.shevtsov.idle.core.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape

class AppShapes(
    progressBar: Shape,
) {
    var progressBar by mutableStateOf(progressBar)
        private set

    fun copy(
        progressBar: Shape = this.progressBar
    ) = AppShapes(
        progressBar = progressBar,
    )

    fun updateShapesFrom(other: AppShapes) {
        progressBar = other.progressBar
    }
}

internal val LocalShapes = staticCompositionLocalOf { devourerShapes() }
