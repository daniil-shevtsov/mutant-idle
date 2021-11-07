package com.daniil.shevtsov.idle.main.ui.shop

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.main.ui.shopStatePreview
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeList

@Preview
@Composable
fun ShopPreview() {
    Shop(shop = shopStatePreview())
}

@Composable
fun Shop(
    shop: ShopState
) {
    UpgradeList(
        upgradeList = shop.upgradeLists.first(),
        onUpgradeSelected = { },
        modifier = Modifier.fillMaxHeight()
    )
}