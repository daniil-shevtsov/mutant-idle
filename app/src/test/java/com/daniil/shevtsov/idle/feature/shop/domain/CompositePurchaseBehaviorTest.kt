package com.daniil.shevtsov.idle.feature.shop.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.util.balanceConfig
import com.daniil.shevtsov.idle.util.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class CompositePurchaseBehaviorTest {

    private val behavior = CompositePurchaseBehavior

    private val resourceStorage = ResourceStorage()

    @BeforeEach
    fun onSetup() {

    }

    @Test
    fun `when buying upgrade - then update its status`() = runBlockingTest {
        val upgradeId = 1L
        val upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = upgradeId, price = 25.0)
            )
        )
        val mutantRatioStorage = MutantRatioStorage()
        val balanceConfig = balanceConfig(resourceSpentForFullMutant = 100.0)

        resourceStorage.setNewValue(75.0)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourceStorage = resourceStorage,
            mutantRatioStorage = mutantRatioStorage,
            upgradeId = upgradeId,
        )

        assertThat(upgradeStorage.getUpgradeById(id = upgradeId))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)
    }

    @Test
    fun `when buying upgrade - then update resource`() = runBlockingTest {
        val upgradeId = 1L
        val upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = upgradeId, price = 25.0)
            )
        )
        val mutantRatioStorage = MutantRatioStorage()
        val balanceConfig = balanceConfig(resourceSpentForFullMutant = 100.0)

        resourceStorage.setNewValue(75.0)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourceStorage = resourceStorage,
            mutantRatioStorage = mutantRatioStorage,
            upgradeId = upgradeId,
        )

        assertThat(resourceStorage.getCurrentValue())
            .isEqualTo(50.0)
    }

    @Test
    fun `when buying upgrade - then update mutant ratio`() = runBlockingTest {
        val upgradeId = 1L
        val upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = upgradeId, price = 10.0)
            )
        )
        val mutantRatioStorage = MutantRatioStorage()
        val balanceConfig = balanceConfig(resourceSpentForFullMutant = 100.0)

        resourceStorage.setNewValue(75.0)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourceStorage = resourceStorage,
            mutantRatioStorage = mutantRatioStorage,
            upgradeId = upgradeId,
        )

        assertThat(mutantRatioStorage.getCurrentValue())
            .isEqualTo(0.10)
    }

    @Test
    fun `when buying upgrade and had some ratio - then increase it`() = runBlockingTest {
        val upgradeId = 1L
        val upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = upgradeId, price = 10.0)
            )
        )
        val mutantRatioStorage = MutantRatioStorage()
        val balanceConfig = balanceConfig(resourceSpentForFullMutant = 100.0)

        resourceStorage.setNewValue(75.0)
        mutantRatioStorage.setNewValue(0.25)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourceStorage = resourceStorage,
            mutantRatioStorage = mutantRatioStorage,
            upgradeId = upgradeId,
        )

        assertThat(mutantRatioStorage.getCurrentValue())
            .isEqualTo(0.35)
    }

    @Test
    fun `when buying nonaffordable upgrade - then don't do anything`() = runBlockingTest {
        val upgradeId = 1L
        val upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = upgradeId, price = 250.0)
            )
        )
        val mutantRatioStorage = MutantRatioStorage()
        val balanceConfig = balanceConfig(resourceSpentForFullMutant = 100.0)
        resourceStorage.setNewValue(75.0)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourceStorage = resourceStorage,
            mutantRatioStorage = mutantRatioStorage,
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