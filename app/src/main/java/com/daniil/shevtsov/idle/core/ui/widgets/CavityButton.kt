package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.settings.view.Protrusive

@Preview
@Composable
fun CavityButtonPreview() {
    CavityButton(text = "Button Title", onClick = {})
}

@Preview
@Composable
fun CavityButtonBigSizePreview() {
    CavityButton(
        text = "Button Title", onClick = {},
        modifier = Modifier
            .height(100.dp)
            .width(200.dp)
    )
}

@Preview
@Composable
fun CavityButtonInBigContainerPreview() {
    Box(
        modifier = Modifier
            .height(100.dp)
            .width(200.dp)
    ) {
        CavityButton(
            text = "Button Title",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CavityButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {
        Text(
            text = text,
            style = AppTheme.typography.button,
            color = AppTheme.colors.backgroundText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(AppTheme.dimensions.paddingSmall)
                .align(Alignment.Center)
        )
    }
) {
    Box(
        modifier = modifier
            .cavitary(
                lightColor = AppTheme.colors.backgroundLight,
                darkColor = AppTheme.colors.backgroundDark,
            )
            .background(AppTheme.colors.backgroundDarkest)
            .padding(1.dp)
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Max),
        contentAlignment = Alignment.Center,
    ) {
        Protrusive(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                }
        ) {
            content()
        }
    }
}
