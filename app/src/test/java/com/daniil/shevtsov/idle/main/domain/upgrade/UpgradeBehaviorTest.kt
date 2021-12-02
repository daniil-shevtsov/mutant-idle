package com.daniil.shevtsov.idle.main.domain.upgrade

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
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

}