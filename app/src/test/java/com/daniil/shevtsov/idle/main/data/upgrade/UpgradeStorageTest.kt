package com.daniil.shevtsov.idle.main.data.upgrade

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
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

}