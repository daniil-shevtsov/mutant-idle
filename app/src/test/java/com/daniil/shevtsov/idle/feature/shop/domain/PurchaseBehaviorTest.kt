package com.daniil.shevtsov.idle.feature.shop.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.util.upgrade
import org.junit.jupiter.api.Test

internal class PurchaseBehaviorTest {

    private val behavior = PurchaseBehavior

    @Test
    fun `should buy upgrade if it's affordable`() {
        val upgrade = upgrade(price = 2.0, status = UpgradeStatus.NotBought)
        val result = behavior.buyUpgrade(upgrade = upgrade, currentResource = Resource(4.0))
        assertThat(result).prop(Upgrade::status).isEqualTo(UpgradeStatus.Bought)
    }

    @Test
    fun `should not buy upgrade if it's not affordable`() {
        val upgrade = upgrade(price = 10.0, status = UpgradeStatus.NotBought)
        val result = behavior.buyUpgrade(upgrade = upgrade, currentResource = Resource(4.0))
        assertThat(result).prop(Upgrade::status).isEqualTo(UpgradeStatus.NotBought)
    }

}