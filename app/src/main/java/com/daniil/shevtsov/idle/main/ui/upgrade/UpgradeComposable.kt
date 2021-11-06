package com.daniil.shevtsov.idle.main.ui.upgrade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.main.ui.Pallete

@Preview
@Composable
fun UpgradePreview() {
    val upgrade = UpgradeModel(
        id = 0L,
        title = "Hand-sword",
        subtitle = "Transform your hand into sharp blade"
    )
    Upgrade(upgrade = upgrade)
}

@Composable
fun Upgrade(upgrade: UpgradeModel) {
    Column(
        modifier = Modifier
            .background(Pallete.Red)
            .padding(4.dp),
    ) {
        Text(
            text = upgrade.title,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Pallete.DarkRed)
                .padding(4.dp)
        )

        Text(
            text = upgrade.subtitle,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(4.dp)
                .padding(bottom = 4.dp)
        )
    }
}