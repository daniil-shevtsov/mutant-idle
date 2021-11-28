package com.daniil.shevtsov.idle.main.data.upgrade

import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UpgradeStorage(
    private val initialUpgrades: List<Upgrade>
) {

    fun observeAll(): Flow<List<Upgrade>> {
        return flowOf(initialUpgrades)
    }

}