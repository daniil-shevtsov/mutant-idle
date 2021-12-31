package com.daniil.shevtsov.idle.feature.shop.domain

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceBehavior
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeBehavior

object CompositePurchaseBehavior {
    suspend fun buyUpgrade(
        balanceConfig: BalanceConfig,
        upgradeStorage: UpgradeStorage,
        resourceStorage: ResourceStorage,
        resourcesStorage: ResourcesStorage,
        mutantRatioStorage: MutantRatioStorage,
        upgradeId: Long
    ) {
        val upgrade = upgradeStorage.getUpgradeById(id = upgradeId)!!

        val currentResource = ResourceBehavior.getCurrentResource(resourcesStorage = resourcesStorage)

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
                resourcesStorage = resourcesStorage,
                amount = upgrade.price.value,
            )
            mutantRatioStorage.setNewValue(mutantRatioStorage.getCurrentValue() + upgrade.price.value / balanceConfig.resourceSpentForFullMutant)
        }
    }
}