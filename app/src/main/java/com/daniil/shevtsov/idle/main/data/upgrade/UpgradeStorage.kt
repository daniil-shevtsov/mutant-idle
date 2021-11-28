package com.daniil.shevtsov.idle.main.data.upgrade

import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class UpgradeStorage(
    initialUpgrades: List<Upgrade>
) {
    private val upgrades: MutableStateFlow<Map<Long, Upgrade>> =
        MutableStateFlow(initialUpgrades.associateBy { it.id })

    fun observeAll(): Flow<List<Upgrade>> {
        return upgrades.map { it.values.toList() }
    }

    fun getUpgradeById(id: Long): Upgrade? {
        val upgradeMap = upgrades.value
        return upgradeMap[id]
    }

}