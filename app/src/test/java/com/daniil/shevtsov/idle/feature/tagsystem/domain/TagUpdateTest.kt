package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.location.domain.location
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TagUpdateTest {
    private data class TestData(
        val selectable: Selectable,
    )

    private val plotHolders = listOf(
        TestData(selectable = action(id = 1L, title = "action")),
        TestData(selectable = location(id = 2L, title = "location")),
        TestData(selectable = upgrade(id = 3L, title = "upgrade")),
    )

    @TestFactory
    fun tests() = plotHolders
        .flatMap { testData ->
            listOf(
                providesTest(testData),
                removesTest(testData),
            )
        }

    private fun providesTest(
        testData: TestData
    ): DynamicTest? =
        DynamicTest.dynamicTest("should add provided tags when ${testData.selectable.title} selected") {
            val providedTag = tag(name = "provided tag")
            val presentTag = tag(name = "present tag")
            val otherTag = tag(name = "other tag")

            val selectable = testData.selectable.copy(
                id = 1L,
                tagRelations = tagRelations(TagRelation.Provides to providedTag)
            )

            val state = mainFunctionalCore(
                state = gameState(
                    selectables = listOf(selectable),
                    player = player(generalTags = listOf(presentTag, otherTag)),
                ),
                viewAction = MainViewAction.SelectableClicked(id = selectable.id)
            )

            assertThat(state)
                .prop(GameState::player)
                .prop(Player::generalTags)
                .containsExactly(
                    presentTag,
                    otherTag,
                    providedTag,
                )
        }

    private fun removesTest(
        testData: TestData
    ): DynamicTest? = DynamicTest.dynamicTest("should remove removable tags when ${testData.selectable.title} selected") {
        val presentTag = tag(name = "present tag")
        val otherTag = tag(name = "other tag")

        val selectable = testData.selectable.copy(
            id = 1L,
            tagRelations = tagRelations(TagRelation.Removes to presentTag)
        )

        val state = mainFunctionalCore(
            state = gameState(
                selectables = listOf(selectable),
                player = player(generalTags = listOf(presentTag, otherTag)),
            ),
            viewAction = MainViewAction.SelectableClicked(id = selectable.id)
        )

        assertThat(state)
            .prop(GameState::player)
            .prop(Player::generalTags)
            .containsExactly(otherTag)
    }
}
