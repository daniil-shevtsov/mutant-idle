package com.daniil.shevtsov.idle.feature.shop.presentation

import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel

data class ShopState(
    val upgradeLists: List<List<UpgradeModel>>
)