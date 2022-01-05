package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary

@Preview
@Composable
fun CavityPreview() {
    Cavity(mainColor = Pallete.Red) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White))
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
                lightColor = Pallete.LightRed,
                darkColor = Pallete.DarkRed
            )
            .background(Pallete.DarkGray)
    ) {
        content()
    }
}