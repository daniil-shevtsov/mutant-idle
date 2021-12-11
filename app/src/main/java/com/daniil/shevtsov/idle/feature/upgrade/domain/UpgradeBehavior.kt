package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import kotlinx.coroutines.flow.Flow

object UpgradeBehavior {

    fun observeAll(storage: UpgradeStorage): Flow<List<Upgrade>> {
        return storage.observeAll()
    }

    fun getById(storage: UpgradeStorage, id: Long): Upgrade? {
        return storage.getUpgradeById(id)
    }

    fun updateById(storage: UpgradeStorage, id: Long, newUpgrade: Upgrade) {
        storage.updateUpgrade(id = id, newValue = newUpgrade)
    }
}