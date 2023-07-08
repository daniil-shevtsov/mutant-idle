package com.daniil.shevtsov.idle.feature.settings.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.initial.domain.createInitialGameState
import org.junit.jupiter.api.Test

class SettingsFunctionalCoreTest {

    @Test
    fun `should create settings categories initially`() {
        val state = createInitialGameState()

        assertThat(state)
            .prop(GameState::settings)
            .all {
                prop(Settings::categories)
                    .extracting(SettingsCategory::id, SettingsCategory::title)
                    .containsExactly(
                        0L to "General",
                        1L to "Accessibility",
                    )
                prop(Settings::selectedCategoryId).isEqualTo(0L)
            }
    }

}
