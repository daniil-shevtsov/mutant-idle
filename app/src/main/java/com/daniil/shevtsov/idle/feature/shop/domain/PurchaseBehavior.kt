package com.daniil.shevtsov.idle.feature.shop.domain

import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus

object PurchaseBehavior {
    fun buyUpgrade(upgrade: Upgrade, currentResource: Resource): Upgrade {
        val newStatus = when {
            upgrade.price.value <= currentResource.value -> UpgradeStatus.Bought
            else -> UpgradeStatus.NotBought
        }
        return upgrade.copy(status = newStatus)
    }
}