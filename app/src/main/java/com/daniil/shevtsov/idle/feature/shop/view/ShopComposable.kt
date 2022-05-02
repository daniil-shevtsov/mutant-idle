package com.daniil.shevtsov.idle.feature.shop.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.shopStatePreviewStub
import com.daniil.shevtsov.idle.core.ui.widgets.Collapsable
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.upgrade.view.UpgradeList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@Preview(
    heightDp = 270
)
@Composable
fun ShopPreview() {
    Column {
        Shop(shop = shopStatePreviewStub(), isCollapsed = false, onToggleCollapse = {})
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Shop(
    shop: ShopState,
    isCollapsed: Boolean,
    modifier: Modifier = Modifier,
    onToggleCollapse: () -> Unit,
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
) {
    Collapsable(
        title = "Upgrades",
        isCollapsed = isCollapsed,
        collapsedContent = { },
        expandedContent = {
            HorizontalPager(
                count = shop.upgradeLists.size,
                modifier = modifier,
            ) { pageIndex ->
                val upgradeList = shop.upgradeLists[pageIndex]
                UpgradeList(
                    upgradeList = upgradeList,
                    onUpgradeSelected = onUpgradeSelected,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        },
        onToggleCollapse = onToggleCollapse,
    )
}
