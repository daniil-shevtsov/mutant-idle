package com.daniil.shevtsov.idle.main.domain.upgrade

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.main.data.upgrade.UpgradeBehavior
import com.daniil.shevtsov.idle.main.data.upgrade.UpgradeStorage
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
            .prop(Upgrade::id)
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

        val upgrade = behavior.getById(storage, 2L)

        assertThat(upgrade)
            .isNull()
    }

}