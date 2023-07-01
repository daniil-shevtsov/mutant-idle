package com.daniil.shevtsov.idle.feature.main.presentation

import assertk.Assert
import assertk.assertThat
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
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
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import com.daniil.shevtsov.idle.feature.upgrade.presentation.SelectableModel
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TagRelationsPresentationTest {
    private data class TestData(
        val selectable: Selectable,
    )

    private val tagToRemove = tag(name = "tag to remove")
    private val tagToProvide = tag(name = "tag to provide")
    private val requiredTag = tag(name = "required tag")
    private val forbiddenTag = tag(name = "forbidden tag")

    private val plotHolders = listOf(
        TestData(selectable = action(id = 1L, title = "action")),
        TestData(selectable = location(id = 2L, title = "location")),
        TestData(selectable = upgrade(id = 3L, title = "upgrade")),
    )

    @TestFactory
    fun testPlotHolders() = plotHolders
        .map { testData ->
            DynamicTest.dynamicTest("should show if required all tags are present") {
                val selectable = testData.selectable
                val availableTag = tag(name = "available tag")
                val unavailableTag = tag(name = "unavailable tag")

                val requiredAllSelectable = selectable.copy(
                    id = 1L,
                    tagRelations = mapOf(TagRelation.RequiredAll to listOf(availableTag))
                )
                val requiredAnyAvailableSelectable = selectable.copy(
                    id = 2L,
                    tagRelations = mapOf(
                        TagRelation.RequiredAny to listOf(
                            availableTag,
                            unavailableTag
                        )
                    ),
                )
                val expectedShownSelectables = listOf(
                    requiredAllSelectable,
                    requiredAnyAvailableSelectable,
                )
                val expectedHiddenSelectables = listOf(
                    selectable.copy(
                        id = 3L,
                        tagRelations = mapOf(
                            TagRelation.RequiredAll to listOf(
                                availableTag,
                                unavailableTag
                            )
                        ),
                    ),
                    selectable.copy(
                        id = 4L,
                        tagRelations = mapOf(TagRelation.RequiredAll to listOf(unavailableTag)),
                    )
                )

                val state = gameState(
                    selectables = expectedShownSelectables + expectedHiddenSelectables,
                    player = player(generalTags = listOf(availableTag)),
                )

                val viewState = mapMainViewState(state = state)

                assertThat(viewState)
                    .extractingMainState()
                    .extractingSelectables(example = selectable)
                    .extracting(SelectableModel::id)
                    .isEqualTo(expectedShownSelectables.map(Selectable::id))
            }
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
