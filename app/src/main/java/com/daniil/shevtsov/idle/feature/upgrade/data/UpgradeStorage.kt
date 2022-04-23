package com.daniil.shevtsov.idle.feature.upgrade.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AppScope
class UpgradeStorage @Inject constructor(
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

    fun updateUpgrade(id: Long, newValue: Upgrade) {
        val upgradeMap = upgrades.value
        val modifiedMap = upgradeMap
            .toMutableMap()
            .apply { put(id, newValue) }
            .toMap()
        upgrades.value = modifiedMap
    }

    fun updateALl(newUpgrades: List<Upgrade>) {
        upgrades.value = newUpgrades.associateBy { it.id }
    }

}
