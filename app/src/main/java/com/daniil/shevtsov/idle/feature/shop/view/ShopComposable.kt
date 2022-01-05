package com.daniil.shevtsov.idle.feature.shop.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.protrusive
import com.daniil.shevtsov.idle.core.ui.shopStatePreviewStub
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
        Shop(shop = shopStatePreviewStub(), isCollapsed = true)
        Shop(shop = shopStatePreviewStub())
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Shop(
    shop: ShopState,
    modifier: Modifier = Modifier,
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
    isCollapsed: Boolean = false,
) {
    if(isCollapsed) {
        Text(
            text = "Upgrades",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Pallete.Red)
                .protrusive(
                    lightColor = Pallete.LightRed,
                    darkColor = Pallete.DarkRed
                )
                .background(Pallete.Red)
                .padding(4.dp)
                .cavitary(
                    lightColor = Pallete.LightRed,
                    darkColor = Pallete.DarkRed
                )
                .background(Pallete.DarkRed)
                .padding(4.dp)
        )
    } else {
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
}