package com.daniil.shevtsov.idle.main.domain.purchase

import com.daniil.shevtsov.idle.main.domain.resource.Resource
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus

fun buyUpgrade(upgrade: Upgrade, currentResource: Resource): Upgrade {
    val newStatus = when {
        upgrade.price.value <= currentResource.value -> UpgradeStatus.Bought
        else -> UpgradeStatus.NotBought
    }
    return upgrade.copy(status = newStatus)
}