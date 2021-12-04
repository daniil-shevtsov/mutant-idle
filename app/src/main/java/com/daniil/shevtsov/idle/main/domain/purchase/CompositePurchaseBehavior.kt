package com.daniil.shevtsov.idle.main.domain.purchase

import com.daniil.shevtsov.idle.main.data.resource.NewResourceBehavior
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.upgrade.UpgradeBehavior
import com.daniil.shevtsov.idle.main.data.upgrade.UpgradeStorage

object CompositePurchaseBehavior {
    suspend fun buyUpgrade(
        upgradeStorage: UpgradeStorage,
        resourceStorage: ResourceStorage,
        upgradeId: Long
    ) {
        val upgrade = upgradeStorage.getUpgradeById(id = upgradeId)!!

        val boughtUpgrade = PurchaseBehavior.buyUpgrade(
            upgrade = upgrade,
            currentResource = NewResourceBehavior.getCurrentResource(resourceStorage)
        )
        UpgradeBehavior.updateById(
            storage = upgradeStorage,
            id = upgradeId,
            newUpgrade =  boughtUpgrade,
        )
        NewResourceBehavior.decreaseResource(
            storage = resourceStorage,
            amount = upgrade.price.value,
        )
    }
}