package com.daniil.shevtsov.idle.feature.shop.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.ratio.data.RatiosStorage
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.util.balanceConfig
import com.daniil.shevtsov.idle.util.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test


internal class CompositePurchaseBehaviorTest {

    private val behavior = CompositePurchaseBehavior

    private val resourcesStorage = ResourcesStorage(
        initialResources = listOf(
            Resource(key = ResourceKey.Blood, name = "Blood", value = 0.0)
        )
    )
    private val ratiosStorage = RatiosStorage(
        initialRatios = listOf(Ratio(key = RatioKey.Mutanity, title = "", value = 0.0))
    )
    private var upgradeStorage = UpgradeStorage(initialUpgrades = emptyList())
    private val mutantRatioStorage = MutantRatioStorage()
    private val balanceConfig = balanceConfig(resourceSpentForFullMutant = 100.0)

    @Test
    fun `when buying upgrade - then update its status`() = runBlockingTest {
        upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = 1L, price = 25.0)
            )
        )

        setInitialResource(75.0)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourcesStorage = resourcesStorage,
            mutantRatioStorage = mutantRatioStorage,
            ratiosStorage = ratiosStorage,
            upgradeId = 1L,
        )

        assertThat(upgradeStorage.getUpgradeById(id = 1L))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)
    }

    @Test
    fun `when buying upgrade - then update resource`() = runBlockingTest {
        upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = 1L, price = 25.0)
            )
        )

        setInitialResource(75.0)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourcesStorage = resourcesStorage,
            mutantRatioStorage = mutantRatioStorage,
            ratiosStorage = ratiosStorage,
            upgradeId = 1L,
        )

        assertFinalResource(50.0)
    }

    @Test
    fun `when buying upgrade - then update mutant ratio`() = runBlockingTest {
        upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = 1L, price = 10.0)
            )
        )

        setInitialResource(75.0)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourcesStorage = resourcesStorage,
            mutantRatioStorage = mutantRatioStorage,
            ratiosStorage = ratiosStorage,
            upgradeId = 1L,
        )

        assertThat(mutantRatioStorage.getCurrentValue())
            .isEqualTo(0.10)
        assertMutanity(expected = 0.10)
    }

    @Test
    fun `when buying upgrade and had some ratio - then increase it`() = runBlockingTest {
        upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = 1L, price = 10.0)
            )
        )

        setInitialResource(75.0)
        mutantRatioStorage.setNewValue(0.25)
        ratiosStorage.updateByKey(key = RatioKey.Mutanity, newRatio = 0.25)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourcesStorage = resourcesStorage,
            mutantRatioStorage = mutantRatioStorage,
            ratiosStorage = ratiosStorage,
            upgradeId = 1L,
        )

        assertThat(mutantRatioStorage.getCurrentValue())
            .isEqualTo(0.35)
        assertMutanity(expected = 0.35)
    }

    @Test
    fun `when trying to buy unaffordable upgrade - then don't do anything`() = runBlockingTest {
        upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = 1L, price = 250.0)
            )
        )
        setInitialResource(75.0)

        behavior.buyUpgrade(
            balanceConfig = balanceConfig,
            upgradeStorage = upgradeStorage,
            resourcesStorage = resourcesStorage,
            mutantRatioStorage = mutantRatioStorage,
            ratiosStorage = ratiosStorage,
            upgradeId = 1L,
        )

        assertThat(upgradeStorage.getUpgradeById(id = 1L))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.NotBought)

        assertFinalResource(75.0)

        assertThat(mutantRatioStorage.getCurrentValue())
            .isEqualTo(0.0)
        assertMutanity(expected = 0.0)
    }

    private suspend fun setInitialResource(
        value: Double
    ) {
        val resource = resourcesStorage.getByKey(key = ResourceKey.Blood)!!
        resourcesStorage.updateByKey(
            key = ResourceKey.Blood,
            newValue = resource.copy(value = value)
        )
    }

    private suspend fun assertFinalResource(value: Double) {
        assertThat(resourcesStorage.getByKey(key = ResourceKey.Blood))
            .isNotNull()
            .prop(Resource::value)
            .isEqualTo(value)
    }

    private fun assertMutanity(expected: Double) {
        assertThat(ratiosStorage.getByKey(key = RatioKey.Mutanity))
            .isNotNull()
            .prop(Ratio::value)
            .isEqualTo(expected)
    }

}