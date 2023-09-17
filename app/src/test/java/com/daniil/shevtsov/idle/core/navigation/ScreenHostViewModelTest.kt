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
import com.daniil.shevtsov.idle.feature.gamestart.presentation.CharacterSelectionViewAction
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewState
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.menu.domain.GameTitleRepository
import com.daniil.shevtsov.idle.feature.menu.domain.GetGameTitleUseCase
import com.daniil.shevtsov.idle.feature.menu.presentation.MainMenuViewState
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleViewState
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private class FakeGameTitleRepository() : GameTitleRepository {
    private val result = MutableStateFlow<String?>(null)

    fun sendResult(title: String) {
        result.value = title
    }

    override suspend fun get(): String {
        return result.filterNotNull().firstOrNull()!!
    }
}

@ExtendWith(MainCoroutineExtension::class)
class ScreenHostViewModelTest {
    private val repository = FakeGameTitleRepository()
    private val getGameTitle = GetGameTitleUseCase(
        repository = repository,
    )
    private val viewModel = ScreenHostViewModel(
        imperativeShell = MainImperativeShell(),
        getGameTitle = getGameTitle,
    )

    @Test
    fun `should show menu screen initially`() = runBlockingTest {
        viewModel.state.test {
            assertThat(awaitItem())
                .all {
                    prop(ScreenHostViewState::speciesId).isEqualTo(Species.Devourer.id)
                    prop(ScreenHostViewState::contentState)
                        .isInstanceOf(ScreenContentViewState.MainMenu::class)
                        .prop(ScreenContentViewState.MainMenu::state)
                        .all {
                            titleIsLoading()
                        }
                }
        }
    }

    @Test
    fun `should show loading of title and then title when got it successfully`() = runBlockingTest {
        viewModel.state.test {
            assertThat(awaitItem())
                .prop(ScreenHostViewState::contentState)
                .isInstanceOf(ScreenContentViewState.MainMenu::class)
                .prop(ScreenContentViewState.MainMenu::state)
                .titleIsLoading()

            repository.sendResult("Server Title")
            assertThat(awaitItem())
                .prop(ScreenHostViewState::contentState)
                .isInstanceOf(ScreenContentViewState.MainMenu::class)
                .prop(ScreenContentViewState.MainMenu::state)
                .title()
                .isEqualTo("Server Title")
        }
    }

    @Test
    fun `should open main screen when start game clicked`() = runBlockingTest {
        viewModel.state.test {
            viewModel.handleAction(ScreenViewAction.CharacterSelection(CharacterSelectionViewAction.StartGame))

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

fun Assert<MainMenuViewState>.titleIsLoading() =
    prop(MainMenuViewState::title).isEqualTo(MenuTitleViewState.Loading)

fun Assert<MainMenuViewState>.title(): Assert<String> = prop(MainMenuViewState::title)
    .isInstanceOf(MenuTitleViewState.Result::class)
    .prop(MenuTitleViewState.Result::text)

