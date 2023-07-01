package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.protrusive
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme

@Composable
fun CavityButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
) {
    Box(
        modifier = modifier
            .cavitary(
                lightColor = AppTheme.colors.backgroundLight,
                darkColor = AppTheme.colors.backgroundDark,
            )
            .background(AppTheme.colors.backgroundDarkest)
            .padding(2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = modifier
                .clickable {
                    onClick()
                }
                .background(AppTheme.colors.background)
                .protrusive(
                    lightColor = AppTheme.colors.backgroundLight,
                    darkColor = AppTheme.colors.backgroundDark,
                )
                .background(AppTheme.colors.background)
                .padding(AppTheme.dimensions.paddingSmall)
                .fillMaxWidth(),
            text = text,
            style = AppTheme.typography.button,
            color = AppTheme.colors.backgroundText,
            textAlign = TextAlign.Center,
        )
    }
}
