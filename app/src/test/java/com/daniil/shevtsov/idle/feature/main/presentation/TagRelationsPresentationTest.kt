package com.daniil.shevtsov.idle.feature.main.presentation

import assertk.Assert
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isNotNull
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.location.domain.location
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tagRelations
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import com.daniil.shevtsov.idle.feature.upgrade.presentation.SelectableModel
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TagRelationsPresentationTest {
    private data class TestData(
        val selectable: Selectable,
    )

    private val plotHolders = listOf(
        TestData(selectable = action(id = 1L, title = "action")),
        TestData(selectable = location(id = 2L, title = "location")),
        TestData(selectable = upgrade(id = 3L, title = "upgrade")),
    )

    @TestFactory
    fun testPlotHolders() = plotHolders
        .flatMap { testData ->
            listOf(
                requireAllTest(testData),
                requireAnyTest(testData),
                requireNoneTest(testData),
                providesTest(testData),
                removesTest(testData),
            )
        }

    private fun requireAllTest(
        testData: TestData
    ): DynamicTest? =
        DynamicTest.dynamicTest("should only show ${testData.selectable.title}s if you have all requiredAll tags") {
            val selectable = testData.selectable
            val availableTag = tag(name = "available tag")
            val unavailableTag = tag(name = "unavailable tag")

            val availableSelectable = selectable.copy(
                id = 1L,
                tagRelations = tagRelations(TagRelation.RequiredAll to availableTag)
            )
            val unavailableSelectable = selectable.copy(
                id = 2L,
                tagRelations = tagRelations(
                    TagRelation.RequiredAll to listOf(
                        availableTag,
                        unavailableTag
                    )
                ),
            )

            val state = gameState(
                selectables = listOf(availableSelectable, unavailableSelectable),
                player = player(generalTags = listOf(availableTag)),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingMainState()
                .extractingSelectables(example = selectable)
                .extracting(SelectableModel::id)
                .containsExactly(availableSelectable.id)
        }

    private fun requireAnyTest(
        testData: TestData
    ): DynamicTest? =
        DynamicTest.dynamicTest("should only show ${testData.selectable.title}s if you have at least one requiredAny tag") {
            val selectable = testData.selectable
            val availableTag = tag(name = "available tag")
            val unavailableTag = tag(name = "unavailable tag")
            val unavailableTag2 = tag(name = "unavailable tag2")

            val availableSelectable = selectable.copy(
                id = 1L,
                tagRelations = tagRelations(
                    TagRelation.RequiredAny to listOf(
                        availableTag,
                        unavailableTag
                    )
                )
            )
            val unavailableSelectable = selectable.copy(
                id = 2L,
                tagRelations = tagRelations(
                    TagRelation.RequiredAny to listOf(
                        unavailableTag2,
                        unavailableTag2
                    )
                ),
            )

            val state = gameState(
                selectables = listOf(availableSelectable, unavailableSelectable),
                player = player(generalTags = listOf(availableTag)),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingMainState()
                .extractingSelectables(example = selectable)
                .extracting(SelectableModel::id)
                .containsExactly(availableSelectable.id)
        }

    private fun requireNoneTest(
        testData: TestData
    ): DynamicTest? =
        DynamicTest.dynamicTest("should hide ${testData.selectable.title} if requireNone tag present") {
            val selectable = testData.selectable
            val presentTag = tag(name = "present tag")
            val missingTag = tag(name = "missing tag")

            val availableSelectable = selectable.copy(
                id = 1L,
                tagRelations = tagRelations(TagRelation.RequiresNone to missingTag)
            )
            val unavailableSelectable = selectable.copy(
                id = 2L,
                tagRelations = tagRelations(TagRelation.RequiresNone to presentTag),
            )

            val state = gameState(
                selectables = listOf(availableSelectable, unavailableSelectable),
                player = player(generalTags = listOf(presentTag)),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingMainState()
                .extractingSelectables(example = selectable)
                .extracting(SelectableModel::id)
                .containsExactly(availableSelectable.id)
        }

    private fun providesTest(
        testData: TestData
    ): DynamicTest? =
        DynamicTest.dynamicTest("should hide ${testData.selectable.title} if only provides what already present") {
            val selectable = testData.selectable
            val presentTag = tag(name = "present tag")
            val missingTag = tag(name = "missing tag")

            val availableSelectable1 = selectable.copy(
                id = 1L,
                tagRelations = tagRelations(TagRelation.Provides to missingTag)
            )
            val availableSelectable2 = selectable.copy(
                id = 1L,
                tagRelations = tagRelations(TagRelation.Provides to listOf(presentTag, missingTag))
            )
            val unavailableSelectable = selectable.copy(
                id = 2L,
                tagRelations = tagRelations(TagRelation.Provides to presentTag),
            )

            val state = gameState(
                selectables = listOf(
                    availableSelectable1,
                    availableSelectable2,
                    unavailableSelectable
                ),
                player = player(generalTags = listOf(presentTag)),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingMainState()
                .extractingSelectables(example = selectable)
                .extracting(SelectableModel::id)
                .containsExactly(availableSelectable1.id, availableSelectable2.id)
        }

    private fun removesTest(
        testData: TestData
    ): DynamicTest? =
        DynamicTest.dynamicTest("should hide ${testData.selectable.title} if only removes what is not present") {
            val selectable = testData.selectable
            val presentTag = tag(name = "present tag")
            val missingTag = tag(name = "missing tag")

            val availableSelectable1 = selectable.copy(
                id = 1L,
                tagRelations = tagRelations(TagRelation.Removes to presentTag)
            )
            val availableSelectable2 = selectable.copy(
                id = 1L,
                tagRelations = tagRelations(TagRelation.Removes to listOf(presentTag, missingTag))
            )
            val unavailableSelectable = selectable.copy(
                id = 2L,
                tagRelations = tagRelations(TagRelation.Removes to missingTag),
            )

            val state = gameState(
                selectables = listOf(
                    availableSelectable1,
                    availableSelectable2,
                    unavailableSelectable
                ),
                player = player(generalTags = listOf(presentTag)),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingMainState()
                .extractingSelectables(example = selectable)
                .extracting(SelectableModel::id)
                .containsExactly(availableSelectable1.id, availableSelectable2.id)
        }

    private fun Assert<MainViewState.Success>.extractingSelectables(example: Selectable): Assert<List<SelectableModel>> =
        transform {
            when (example) {
                is Action -> it.actionState.actionPanes[0].actions
                is Location -> it.locationSelectionViewState.locations
                is Upgrade -> it.shop.upgrades
                else -> null
            }
        }
            .isNotNull()
}
