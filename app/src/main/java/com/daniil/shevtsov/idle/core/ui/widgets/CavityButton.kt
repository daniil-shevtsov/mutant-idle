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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.protrusive

@Composable
fun CavityButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
) {
    Box(
        modifier = modifier
            .cavitary(
                lightColor = Pallete.BackgroundLight,
                darkColor = Pallete.BackgroundDark,
            )
            .background(Pallete.BackgroundDarkest)
            .padding(2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = modifier
                .clickable {
                    onClick()
                }
                .background(Pallete.Background)
                .protrusive(
                    lightColor = Pallete.BackgroundLight,
                    darkColor = Pallete.BackgroundDark,
                )
                .background(Pallete.Background)
                .padding(4.dp)
                .fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            color = Color.White,
        )
    }
}
