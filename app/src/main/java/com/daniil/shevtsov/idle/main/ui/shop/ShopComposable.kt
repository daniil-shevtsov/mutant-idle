package com.daniil.shevtsov.idle.main.ui.shop

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.main.ui.shopStatePreview
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@Preview
@Composable
fun ShopPreview() {
    Shop(shop = shopStatePreview())
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Shop(
    shop: ShopState,
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
) {
    HorizontalPager(count = shop.upgradeLists.size) { pageIndex ->
        val upgradeList = shop.upgradeLists[pageIndex]
        UpgradeList(
            upgradeList = upgradeList,
            onUpgradeSelected = onUpgradeSelected,
            modifier = Modifier.fillMaxHeight()
        )
    }


}