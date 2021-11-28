package com.daniil.shevtsov.idle.main.domain.purchase

import com.daniil.shevtsov.idle.main.domain.resource.Resource
import com.daniil.shevtsov.idle.main.domain.upgrade.Price
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade

fun buyUpgrade(upgrade: Upgrade, forPrice: Price, whileResourceIs: Resource): Upgrade {
    return upgrade
}