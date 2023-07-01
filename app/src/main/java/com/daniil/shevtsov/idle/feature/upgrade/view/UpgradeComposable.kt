package com.daniil.shevtsov.idle.feature.upgrade.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    onClicked = { onUpgradeSelected(upgrade.id) }
                )
            }
        },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
        contentPadding = PaddingValues(AppTheme.dimensions.paddingSmall)
    )
}

@Composable
fun Upgrade(
    upgrade: UpgradeModel,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {},
) {
    val priceColor = when (upgrade.status) {
        UpgradeStatusModel.Affordable -> AppTheme.colors.textLight
        UpgradeStatusModel.NotAffordable -> AppTheme.colors.textDark
        UpgradeStatusModel.Bought -> AppTheme.colors.textLight
    }
    val priceText = when (upgrade.status) {
        UpgradeStatusModel.Bought -> "Bought"
        else -> upgrade.price.value
    }
    Column(
        modifier = modifier
            .background(AppTheme.colors.background)
            .clickable { onClicked() }
            .padding(AppTheme.dimensions.paddingSmall)
            .background(AppTheme.colors.backgroundDark),
    ) {
        Box {
            Text(
                text = upgrade.title,
                style = AppTheme.typography.title,
                color = AppTheme.colors.textLight,
                modifier = modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.backgroundDark)
                    .padding(AppTheme.dimensions.paddingSmall)
            )
            Text(
                text = priceText,
                style = AppTheme.typography.title,
                color = priceColor,
                modifier = modifier
                    .background(AppTheme.colors.backgroundDark)
                    .padding(AppTheme.dimensions.paddingSmall)
                    .align(Alignment.CenterEnd)
            )
        }

        Text(
            text = upgrade.subtitle,
            style = AppTheme.typography.body,
            color = AppTheme.colors.textDark,
            modifier = modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundText)
                .padding(AppTheme.dimensions.paddingSmall)
                .padding(bottom = 4.dp)
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(AppTheme.dimensions.paddingSmall),
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
