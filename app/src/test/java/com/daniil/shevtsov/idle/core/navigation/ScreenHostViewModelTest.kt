package com.daniil.shevtsov.idle.core.navigation

import app.cash.turbine.test
import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewState
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleViewState
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
                            title().isEqualTo("Mutant Idle")
                        }
                }
        }
    }

    @Test
    fun `should open main screen when start game clicked`() = runBlockingTest {
        viewModel.state.test {
            viewModel.handleAction(ScreenViewAction.Start(GameStartViewAction.StartGame))

            assertThat(expectMostRecentItem())
                .prop(ScreenHostViewState::contentState)
                .isInstanceOf(ScreenContentViewState.Main::class)
                .prop(ScreenContentViewState.Main::state)
                .isInstanceOf(MainViewState.Success::class)
                .all {
                    prop(MainViewState.Success::sectionCollapse).containsAll(
                        SectionKey.Resources to false,
                        SectionKey.Actions to false,
                        SectionKey.Upgrades to false,
                    )
                }
        }
    }
}

fun Assert<MenuViewState>.title(): Assert<String> = prop(MenuViewState::title)
    .isInstanceOf(MenuTitleViewState.Result::class)
    .prop(MenuTitleViewState.Result::text)

