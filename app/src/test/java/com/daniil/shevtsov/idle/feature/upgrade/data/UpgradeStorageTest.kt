package com.daniil.shevtsov.idle.feature.upgrade.data

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.util.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class UpgradeStorageTest {
    private lateinit var storage: UpgradeStorage

    @BeforeEach
    fun onSetup() {
        storage = UpgradeStorage(emptyList())
    }

    @Test
    fun `should return initial upgrades`() = runBlockingTest {
        val initialUpgrades = listOf(upgrade(id = 0), upgrade(id = 1))
        storage = UpgradeStorage(initialUpgrades)

        storage.observeAll().test {
            assertThat(awaitItem()).isEqualTo(initialUpgrades)
        }
    }

    @Test
    fun `should return upgrade by id if it's present`() = runBlockingTest {
        val initialUpgrades = listOf(upgrade(id = 0), upgrade(id = 1))
        storage = UpgradeStorage(initialUpgrades)

        val upgrade = storage.getUpgradeById(1)

        assertThat(upgrade)
            .isNotNull()
            .prop(Upgrade::id)
            .isEqualTo(1)
    }

    @Test
    fun `should return null by id if no such upgrade`() = runBlockingTest {
        val initialUpgrades = listOf(upgrade(id = 0), upgrade(id = 1))
        storage = UpgradeStorage(initialUpgrades)

        val upgrade = storage.getUpgradeById(4)

        assertThat(upgrade)
            .isNull()
    }

    @Test
    fun `should update upgrade by id`() = runBlockingTest {
        val initialUpgrades = listOf(upgrade(id = 0, status = UpgradeStatus.NotBought))
        storage = UpgradeStorage(initialUpgrades)

        val upgrade = upgrade(id = 0, status = UpgradeStatus.Bought)
        storage.updateUpgrade(id = 0, newValue = upgrade)

        assertThat(storage.getUpgradeById(id = 0))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)
    }

}