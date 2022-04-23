package com.daniil.shevtsov.idle.feature.main.domain

import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.util.resource
import com.daniil.shevtsov.idle.util.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainFunctionalCoreTest {
    @Test
    fun `should buy upgrade when clicked and affordable`() = runBlockingTest {
        val initialState = mainFunctionalCoreState(
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 10.0),
            ),
            upgrades = listOf(
                upgrade(id = 0L, price = 5.0)
            ),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity)
            )
        )

        val newState = mainFunctionalCore(
            state = initialState,
            action = MainViewAction.UpgradeSelected(id = 0L),
        )

        assertThat(newState)
            .prop(MainFunctionalCoreState::upgrades)
            .extracting(Upgrade::id, Upgrade::status)
            .containsExactly(0L to UpgradeStatus.Bought)
    }
}
