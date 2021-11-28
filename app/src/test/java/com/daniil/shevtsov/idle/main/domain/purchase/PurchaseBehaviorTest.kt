package com.daniil.shevtsov.idle.main.domain.purchase

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.main.domain.resource.Resource
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus
import com.daniil.shevtsov.idle.util.upgrade
import org.junit.jupiter.api.Test

internal class PurchaseBehaviorTest {

    @Test
    fun `should buy upgrade if it's affordable`() {
        val upgrade = upgrade(price = 2.0, status = UpgradeStatus.NotBought)
        val result = buyUpgrade(upgrade, forPrice = upgrade.price, whileResourceIs = Resource(4.0))
        assertThat(result).prop(Upgrade::status).isEqualTo(UpgradeStatus.Bought)
    }

}