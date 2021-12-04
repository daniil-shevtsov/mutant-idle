package com.daniil.shevtsov.idle.main.data.upgrade

import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import kotlinx.coroutines.flow.Flow

object UpgradeBehavior {

    fun observeAll(storage: UpgradeStorage): Flow<List<Upgrade>> {
        return storage.observeAll()
    }

    fun getById(storage: UpgradeStorage, id: Long): Upgrade? {
        return storage.getUpgradeById(id)
    }
}