package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme

@Preview
@Composable
fun CavityPreview() {
    Cavity(mainColor = AppTheme.colors.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp)
                .background(AppTheme.colors.background)
        )
    }
}

@Composable
fun Cavity(
    mainColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(mainColor)
            .cavitary(
                lightColor = AppTheme.colors.backgroundLight,
                darkColor = AppTheme.colors.backgroundDark
            )
            .background(AppTheme.colors.backgroundDarkest)
    ) {
        content()
    }
}
