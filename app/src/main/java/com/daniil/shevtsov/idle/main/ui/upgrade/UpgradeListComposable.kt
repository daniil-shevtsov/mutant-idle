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

@Preview
@Composable
fun UpgradeListPreview() {
    val upgrades = listOf(
        UpgradeModel(
            id = 0L,
            title = "Hand-sword",
            subtitle = "Transform your hand into a sharp blade"
        ),
        UpgradeModel(
            id = 1L,
            title = "Fangs",
            subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though"
        ),
        UpgradeModel(
            id = 2L,
            title = "Iron jaws",
            subtitle = "Your jaws become stronger than any shark"
        ),
    )

    UpgradeList(upgradeList = upgrades)
}

@Composable
fun UpgradeList(upgradeList: List<UpgradeModel>) {
    LazyColumn(
        content = {
            items(items = upgradeList) { upgrade ->
                Upgrade(upgrade)
            }
        },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(Pallete.DarkGray)
            .padding(4.dp)
    )
}