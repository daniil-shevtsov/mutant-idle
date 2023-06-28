package com.daniil.shevtsov.idle.feature.main.presentation

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.flavor.flavor
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.location.domain.location
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import com.daniil.shevtsov.idle.feature.upgrade.presentation.FlavoredModel
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class FlavorViewStateTest {
    private data class FlavorTestData(
        val flavorable: Flavorable,
    )

    private val plotHolders = listOf(
        FlavorTestData(flavorable = action(id = 1L, title = "action")),
        FlavorTestData(flavorable = location(id = 2L, title = "location")),
        FlavorTestData(flavorable = upgrade(id = 3L, title = "upgrade")),
    )

    @TestFactory
    fun testPlotHolders() = plotHolders
        .map { flavorTestData ->
            DynamicTest.dynamicTest("should replace all ${flavorTestData.flavorable.title} placeholders with flavor in all text fields") {
                val tag = tag(name = "flavor tag")
                val flavoredTitle = "flavoredTitle"
                val flavoredSubtitle = "flavoredSubtitle"
                val titleFlavor = flavor(
                    placeholder = "${Flavors.PREFIX}title",
                    values = mapOf(tag to flavoredTitle),
                )
                val subtitleFlavor = flavor(
                    placeholder = "${Flavors.PREFIX}subtitle",
                    values = mapOf(tag to flavoredSubtitle),
                )

                val flavorable = flavorTestData.flavorable.copy(
                    title = titleFlavor.placeholder,
                    subtitle = subtitleFlavor.placeholder,
                )

                val state = gameState(
                    selectables = listOf(flavorable as Selectable),
                    flavors = listOf(titleFlavor, subtitleFlavor),
                    player = player(
                        generalTags = listOf(tag)
                    ),
                )

                val viewState = mapMainViewState(state = state)

                assertThat(viewState)
                    .extractingMainState()
                    .extractingFlavored(example = flavorable)
                    .index(0)
                    .all {
                        prop(FlavoredModel::title).isEqualTo(flavoredTitle)
                        prop(FlavoredModel::subtitle).isEqualTo(flavoredSubtitle)
                    }
            }
        }
}

private fun Assert<MainViewState.Success>.extractingFlavored(example: Flavorable): Assert<List<FlavoredModel>> =
    transform {
        when (example) {
            is Action -> it.actionState.actionPanes[0].actions
            is Location -> it.locationSelectionViewState.locations
            is Upgrade -> it.shop.upgrades
            else -> null
        }
    }
        .isNotNull()

