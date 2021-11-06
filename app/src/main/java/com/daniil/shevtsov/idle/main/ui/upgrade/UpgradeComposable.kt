package com.daniil.shevtsov.idle.main.ui.upgrade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.main.ui.Pallete
import com.daniil.shevtsov.idle.main.ui.upgradePreview

@Preview
@Composable
fun UpgradePreview() {
    Column {
        UpgradeStatusModel.values().forEach { status ->
            Upgrade(upgrade = upgradePreview(status = status))
        }
    }

}

@Composable
fun Upgrade(upgrade: UpgradeModel) {
    val priceColor = when (upgrade.status) {
        UpgradeStatusModel.Affordable -> Color.White
        UpgradeStatusModel.NotAffordable -> Color.Black
        UpgradeStatusModel.Bought -> Color.White
    }
    val priceText = when (upgrade.status) {
        UpgradeStatusModel.Bought -> "Bought"
        else -> "100"
    }
    Column(
        modifier = Modifier
            .background(Pallete.Red)
            .padding(4.dp),
    ) {
        Box {
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
                text = priceText,
                color = priceColor,
                fontSize = 24.sp,
                modifier = Modifier
                    .background(Pallete.DarkRed)
                    .padding(4.dp)
                    .align(Alignment.CenterEnd)
            )
        }

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