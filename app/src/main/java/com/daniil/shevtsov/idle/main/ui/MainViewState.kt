package com.daniil.shevtsov.idle.main.ui

import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModel

sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(
        val resource: ResourceModel,
        val upgrades: List<UpgradeModel>,
    ) : MainViewState()
}