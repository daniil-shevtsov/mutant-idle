package com.daniil.shevtsov.idle.core.navigation

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuViewState
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
class ScreenHostViewModelTest {
    private val viewModel = ScreenHostViewModel(imperativeShell = MainImperativeShell())

    @Test
    fun `should show menu screen initially`() = runBlockingTest {
        viewModel.state.test {
            assertThat(awaitItem())
                .all {
                    prop(ScreenHostViewState::speciesId).isEqualTo(Species.Devourer.id)
                    prop(ScreenHostViewState::contentState)
                        .isInstanceOf(ScreenContentViewState.Menu::class)
                        .prop(ScreenContentViewState.Menu::state)
                        .all {
                            prop(MenuViewState::title).isEqualTo("Mutant Idle")
                        }
                }
        }
    }
}
