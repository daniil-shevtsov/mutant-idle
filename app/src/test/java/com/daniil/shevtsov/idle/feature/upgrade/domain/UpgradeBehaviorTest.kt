package com.daniil.shevtsov.idle.feature.upgrade.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.util.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UpgradeBehaviorTest {

    private lateinit var behavior: UpgradeBehavior

    @BeforeEach
    fun onSetup() {
        behavior = UpgradeBehavior
    }

    @Test
    fun `when observe - then return initial upgrades`() = runBlockingTest {
        val initialUpgrades = listOf(
            upgrade(id = 0L),
            upgrade(id = 1L),
        )
        val storage = UpgradeStorage(
            initialUpgrades = initialUpgrades
        )
        behavior.observeAll(storage = storage).test {
            assertThat(awaitItem())
                .isEqualTo(initialUpgrades)
        }
    }

    @Test
    fun `when get by id and present - then return it`() = runBlockingTest {
        val initialUpgrades = listOf(
            upgrade(id = 0L),
            upgrade(id = 1L),
        )
        val storage = UpgradeStorage(
            initialUpgrades = initialUpgrades
        )

        val upgrade = behavior.getById(storage, 0L)

        assertThat(upgrade)
            .prop("NAME") { Upgrade::id.call(it) }
            .isEqualTo(0L)
    }

    @Test
    fun `when get by id and not present - then return null`() = runBlockingTest {
        val initialUpgrades = listOf(
            upgrade(id = 0L),
            upgrade(id = 1L),
        )
        val storage = UpgradeStorage(
            initialUpgrades = initialUpgrades
        )

        val upgrade = behavior.getById(storage = storage, id = 2L)

        assertThat(upgrade)
            .isNull()
    }

    @Test
    fun `when update by id and present - then update it`() = runBlockingTest {
        val initialUpgrades = listOf(upgrade(id = 0, status = UpgradeStatus.NotBought))
        val storage = UpgradeStorage(initialUpgrades)

        val newUpgrade = upgrade(id = 0, status = UpgradeStatus.Bought)

        behavior.updateById(storage = storage, id = 0L, newUpgrade = newUpgrade)

        assertThat(storage.getUpgradeById(id = 0))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)
    }

}