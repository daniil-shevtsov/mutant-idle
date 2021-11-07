package com.daniil.shevtsov.idle.main.ui.upgrade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.main.ui.Pallete
import com.daniil.shevtsov.idle.main.ui.upgradeListPreview

@Preview
@Composable
fun UpgradeListPreview() {
    UpgradeList(upgradeList = upgradeListPreview())
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
        modifier = modifier
            .background(Pallete.DarkGray)
            .padding(4.dp)
    )
}