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
import com.daniil.shevtsov.idle.feature.menu.domain.GameTitleRepository
import com.daniil.shevtsov.idle.feature.menu.domain.GetGameTitleUseCase
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleViewState
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuViewState
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

    var completedRequestCount = 0

    fun sendResult(title: String) {
        result.value = title
        completedRequestCount++
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
                        .isInstanceOf(ScreenContentViewState.Menu::class)
                        .prop(ScreenContentViewState.Menu::state)
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
                .isInstanceOf(ScreenContentViewState.Menu::class)
                .prop(ScreenContentViewState.Menu::state)
                .titleIsLoading()

            repository.sendResult("Server Title")
            assertThat(awaitItem())
                .prop(ScreenHostViewState::contentState)
                .isInstanceOf(ScreenContentViewState.Menu::class)
                .prop(ScreenContentViewState.Menu::state)
                .title()
                .isEqualTo("Server Title")
            assertThat(repository.completedRequestCount).isEqualTo(1)
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

    @Test
    fun `should cancel request when cancel action`() = runBlockingTest {
        viewModel.state.test {
            assertThat(awaitItem())
                .prop(ScreenHostViewState::contentState)
                .isInstanceOf(ScreenContentViewState.Menu::class)
                .prop(ScreenContentViewState.Menu::state)
                .titleIsLoading()

            viewModel.handleAction(ScreenViewAction.Start(GameStartViewAction.CancelClicked))
            repository.sendResult("Server Title")
            assertThat(repository.completedRequestCount).isEqualTo(0)
        }
    }
}

fun Assert<MenuViewState>.titleIsLoading() =
    prop(MenuViewState::title).isEqualTo(MenuTitleViewState.Loading)

fun Assert<MenuViewState>.title(): Assert<String> = prop(MenuViewState::title)
    .isInstanceOf(MenuTitleViewState.Result::class)
    .prop(MenuTitleViewState.Result::text)

