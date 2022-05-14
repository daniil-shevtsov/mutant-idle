package com.daniil.shevtsov.idle.feature.upgrade.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme

import com.daniil.shevtsov.idle.core.ui.upgradeListPreviewStub
import com.daniil.shevtsov.idle.core.ui.upgradePreviewStub
import com.daniil.shevtsov.idle.feature.ratio.view.RatioChanges
import com.daniil.shevtsov.idle.feature.resource.view.ResourceChanges
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel

@Preview
@Composable
fun UpgradePreview() {
    Upgrade(upgrade = upgradePreviewStub(status = UpgradeStatusModel.Affordable))
}

@Preview(heightDp = 270)
@Composable
fun UpgradeListPreview() {
    UpgradeList(upgradeList = upgradeListPreviewStub())
}

@Composable
fun UpgradeList(
    upgradeList: List<UpgradeModel>,
    modifier: Modifier = Modifier,
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
) {
    LazyColumn(
        content = {
            items(items = upgradeList) { upgrade ->
                Upgrade(
                    upgrade = upgrade,
                    modifier = modifier,
                    onClicked = { onUpgradeSelected(upgrade.id) }
                )
            }
        },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    )
}

@Composable
fun Upgrade(
    upgrade: UpgradeModel,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {},
) {
    val priceColor = when (upgrade.status) {
        UpgradeStatusModel.Affordable -> Color.White
        UpgradeStatusModel.NotAffordable -> Color.Black
        UpgradeStatusModel.Bought -> Color.White
    }
    val priceText = when (upgrade.status) {
        UpgradeStatusModel.Bought -> "Bought"
        else -> upgrade.price.value
    }
    Column(
        modifier = modifier
            .background(AppTheme.colors.background)
            .clickable { onClicked() }
            .padding(4.dp)
            .background(AppTheme.colors.backgroundDark),
    ) {
        Box {
            Text(
                text = upgrade.title,
                color = Color.White,
                fontSize = 24.sp,
                modifier = modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.backgroundDark)
                    .padding(4.dp)
            )
            Text(
                text = priceText,
                color = priceColor,
                fontSize = 24.sp,
                modifier = modifier
                    .background(AppTheme.colors.backgroundDark)
                    .padding(4.dp)
                    .align(Alignment.CenterEnd)
            )
        }

        Text(
            text = upgrade.subtitle,
            fontSize = 16.sp,
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(4.dp)
                .padding(bottom = 4.dp)
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ResourceChanges(
                resourceChanges = upgrade.resourceChanges,
                modifier = modifier,
            )
            RatioChanges(
                ratioChanges = upgrade.ratioChanges,
                modifier = modifier,
            )
        }
    }
}
