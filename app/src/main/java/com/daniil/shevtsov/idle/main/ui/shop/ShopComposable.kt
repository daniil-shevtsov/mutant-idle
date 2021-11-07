package com.daniil.shevtsov.idle.main.ui.shop

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.main.ui.shopStatePreviewStub
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@Preview
@Composable
fun ShopPreview() {
    Shop(shop = shopStatePreviewStub())
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Shop(
    shop: ShopState,
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
    modifier: Modifier = Modifier,
) {
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
}