package com.daniil.shevtsov.idle.feature.shop.domain

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.ratio.data.RatiosStorage
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceBehavior
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeBehavior

object CompositePurchaseBehavior {
    suspend fun buyUpgrade(
        balanceConfig: BalanceConfig,
        upgradeStorage: UpgradeStorage,
        resourcesStorage: ResourcesStorage,
        mutantRatioStorage: MutantRatioStorage,
        ratiosStorage: RatiosStorage,
        upgradeId: Long,
    ) {
        val upgrade = upgradeStorage.getUpgradeById(id = upgradeId)!!

        val currentResource = ResourceBehavior.getCurrentResource(
            resourcesStorage = resourcesStorage,
            resourceKey = ResourceKey.Blood,
        )

        if (upgrade.price.value <= currentResource.value) {
            val boughtUpgrade = PurchaseBehavior.buyUpgrade(
                upgrade = upgrade,
                currentResource = currentResource,
            )
            UpgradeBehavior.updateById(
                storage = upgradeStorage,
                id = upgradeId,
                newUpgrade = boughtUpgrade,
            )
            ResourceBehavior.decreaseResource(
                resourcesStorage = resourcesStorage,
                resourceKey = ResourceKey.Blood,
                amount = upgrade.price.value,
            )

            val oldBlood = mutantRatioStorage.getCurrentValue()
            val bloodGain = upgrade.price.value / balanceConfig.resourceSpentForFullMutant
            val newBlood = oldBlood + bloodGain

            val oldKekRatio = ratiosStorage.getByKey(key = RatioKey.Mutanity)!!
            mutantRatioStorage.setNewValue(newBlood)
            ratiosStorage.updateByKey(
                key = RatioKey.Mutanity,
                newValue = oldKekRatio.copy(value = newBlood)
            )
        }
    }
}