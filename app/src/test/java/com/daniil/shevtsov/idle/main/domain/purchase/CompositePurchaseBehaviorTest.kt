package com.daniil.shevtsov.idle.main.domain.purchase

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.upgrade.UpgradeStorage
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus
import com.daniil.shevtsov.idle.util.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

internal class CompositePurchaseBehaviorTest {

    private val behavior = CompositePurchaseBehavior

    @Test
    fun `when buying affordable upgrade - then update it and resource`() = runBlockingTest {
        val upgradeId = 1L
        val resourceStorage = ResourceStorage()
        val upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = upgradeId, price = 25.0)
            )
        )
        resourceStorage.setNewValue(75.0)

        behavior.buyUpgrade(
            upgradeStorage = upgradeStorage,
            resourceStorage = resourceStorage,
            upgradeId = upgradeId,
        )

        assertThat(upgradeStorage.getUpgradeById(id = upgradeId))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)

        assertThat(resourceStorage.getCurrentValue())
            .isEqualTo(50.0)
    }

    @Test
    fun `when buying nonaffordable upgrade - then don't do anything`() = runBlockingTest {
        val upgradeId = 1L
        val resourceStorage = ResourceStorage()
        val upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = upgradeId, price = 250.0)
            )
        )
        resourceStorage.setNewValue(75.0)

        behavior.buyUpgrade(
            upgradeStorage = upgradeStorage,
            resourceStorage = resourceStorage,
            upgradeId = upgradeId,
        )

        assertThat(upgradeStorage.getUpgradeById(id = upgradeId))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.NotBought)

        assertThat(resourceStorage.getCurrentValue())
            .isEqualTo(75.0)
    }

}