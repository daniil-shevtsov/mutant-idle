package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.protrusive
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme

@Composable
fun MyProgressBar(
    progressPercentage: Float,
    modifier: Modifier = Modifier,
) {
    val height = remember { 30.dp }
    Box(modifier = modifier) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .cavitary(
                    lightColor = AppTheme.colors.backgroundLight,
                    darkColor = AppTheme.colors.backgroundDark
                )
                .background(AppTheme.colors.backgroundText),
        )
        Box(
            modifier = modifier
                .fillMaxWidth(fraction = progressPercentage)
                .height(height)
                .padding(4.dp)
                .protrusive(
                    lightColor = AppTheme.colors.backgroundLight,
                    darkColor = AppTheme.colors.backgroundDark
                )
                .background(AppTheme.colors.background),
        )
    }
}
