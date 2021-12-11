package com.daniil.shevtsov.idle.feature.shop.domain

import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.resource.domain.NewResourceBehavior
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeBehavior

object CompositePurchaseBehavior {
    suspend fun buyUpgrade(
        upgradeStorage: UpgradeStorage,
        resourceStorage: ResourceStorage,
        upgradeId: Long
    ) {
        val upgrade = upgradeStorage.getUpgradeById(id = upgradeId)!!

        val currentResource = NewResourceBehavior.getCurrentResource(resourceStorage)

        if(upgrade.price.value <= currentResource.value) {
            val boughtUpgrade = PurchaseBehavior.buyUpgrade(
                upgrade = upgrade,
                currentResource = currentResource,
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
}