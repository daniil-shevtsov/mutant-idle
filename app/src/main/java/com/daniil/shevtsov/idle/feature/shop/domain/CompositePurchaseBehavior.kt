package com.daniil.shevtsov.idle.feature.shop.domain

import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceBehavior
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeBehavior

object CompositePurchaseBehavior {
    suspend fun buyUpgrade(
        upgradeStorage: UpgradeStorage,
        resourceStorage: ResourceStorage,
        mutantRatioStorage: MutantRatioStorage,
        upgradeId: Long
    ) {
        val upgrade = upgradeStorage.getUpgradeById(id = upgradeId)!!

        val currentResource = ResourceBehavior.getCurrentResource(resourceStorage)

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
            ResourceBehavior.decreaseResource(
                storage = resourceStorage,
                amount = upgrade.price.value,
            )
            mutantRatioStorage.setNewValue(0.0)
        }
    }
}