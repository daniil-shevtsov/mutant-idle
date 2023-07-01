package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.RatioChanges
import com.daniil.shevtsov.idle.feature.action.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.action.domain.TagRelations
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus

fun mainFunctionalCore(
    state: GameState,
    viewAction: MainViewAction,
): GameState {
    val newState = when (viewAction) {
        is MainViewAction.SelectableClicked -> handleSelectableClicked(
            state = state,
            viewAction = viewAction,
        )

        is MainViewAction.ToggleSectionCollapse -> handleSectionCollapsed(
            state = state,
            viewAction = viewAction,
        )

        is MainViewAction.LocationSelectionExpandChange -> handleLocationSelectionExpandChange(
            state = state,
        )

        MainViewAction.Init -> state
    }
    return newState
}

fun handleLocationSelectionExpandChange(
    state: GameState
): GameState {
    return state.copy(
        locationSelectionState = state.locationSelectionState.copy(
            isSelectionExpanded = !state.locationSelectionState.isSelectionExpanded
        ),
    )
}

fun handleLocationSelected(
    state: GameState,
    selectedLocation: Location
): GameState {
    return state.copy(
        locationSelectionState = state.locationSelectionState.copy(
            selectedLocation = selectedLocation,
        ),
        player = state.player.copy(
            generalTags = updateTags(state.player.generalTags, selectedLocation.tags)
        )
    ).addPlotEntry(selectedLocation)

}

interface PlotHolder {
    val plot: String?

    fun createDefaultPlot(): String
    fun copy(plot: String?): PlotHolder
}

interface Selectable {
    val id: Long
    val title: String

    fun copy(
        id: Long? = null,
        title: String? = null,
    ): Selectable
}

private fun GameState.addPlotEntry(
    plotHolder: PlotHolder
): GameState {
    val entry = PlotEntry(plotHolder.plot ?: plotHolder.createDefaultPlot())
    return copy(plotEntries = plotEntries + entry)
}


fun handleDrawerTabSwitched(
    state: GameState,
    viewAction: DrawerViewAction.TabSwitched
): GameState {
    return state.copy(
        drawerTabs = state.drawerTabs.map { tab ->
            tab.copy(isSelected = tab.id == viewAction.id)
        }
    )
}

fun handleSectionCollapsed(
    state: GameState,
    viewAction: MainViewAction.ToggleSectionCollapse,
): GameState {
    return state.copy(
        sections = state.sections.map { section ->
            section.copy(
                isCollapsed = when (section.key) {
                    viewAction.key -> !section.isCollapsed
                    else -> section.isCollapsed
                }
            )
        }
    )
}

private fun handleActionClicked(
    state: GameState,
    selectedAction: Action,
): GameState {
    val hasInvalidChanges = hasInvalidChanges(
        currentResources = state.resources,
        resourceChanges = selectedAction.resourceChanges,
    )
    val updatedResources = applyResourceChanges(
        currentResources = state.resources,
        resourceChanges = selectedAction.resourceChanges
    )

    val updatedRatios = applyRatioChanges(
        currentRatios = state.ratios,
        ratioChanges = selectedAction.ratioChanges,
        tags = state.player.tags,
    )

    return if (!hasInvalidChanges) {
        state.copy(
            ratios = updatedRatios,
            resources = updatedResources,
            player = state.player.copy(
                generalTags = updateTags(state.player.generalTags, selectedAction.tags)
            ),
            currentScreen = when {
                (updatedRatios.find { it.key == RatioKey.Suspicion }?.value
                    ?: 0.0) >= 1.0 -> Screen.FinishedGame

                else -> state.currentScreen
            }
        ).addPlotEntry(selectedAction)
    } else {
        state
    }
}

private fun applyRatioChanges(
    currentRatios: List<Ratio>,
    ratioChanges: RatioChanges,
    tags: List<Tag>,
): List<Ratio> = currentRatios.map { ratio ->
    val ratioChange = ratioChanges[ratio.key]
        ?.minByOrNull { (matchedTags, _) ->
            (tags - matchedTags.toSet()).size
        }?.value
    when (ratioChange) {
        null -> ratio
        else -> ratio.copy(value = ratio.value + ratioChange)
    }
}

private fun applyResourceChanges(
    currentResources: List<Resource>,
    resourceChanges: ResourceChanges,
) = currentResources.map { resource ->
    when (val resourceChange = resourceChanges[resource.key]) {
        null -> resource
        else -> resource.copy(value = resource.value + resourceChange)
    }
}

private fun hasInvalidChanges(
    currentResources: List<Resource>,
    resourceChanges: ResourceChanges,
): Boolean = resourceChanges.any { (resourceKey, resourceChange) ->
    val currentResourceValue = currentResources
        .find { resource -> resource.key == resourceKey }!!.value

    currentResourceValue + resourceChange < 0
}

private fun handleUpgradeSelected(
    state: GameState,
    upgradeToBuy: Upgrade,
): GameState {
    val hasInvalidChanges = hasInvalidChanges(
        currentResources = state.resources,
        resourceChanges = upgradeToBuy.resourceChanges,
    )

    return when {
        !hasInvalidChanges -> {
            val boughtUpgrade = upgradeToBuy.copy(status = UpgradeStatus.Bought)

            val updatedUpgrades = state.upgrades.map { upgrade ->
                if (upgrade.id == boughtUpgrade.id) {
                    boughtUpgrade
                } else {
                    upgrade
                }
            }.associateBy { it.id }

            val updatedResources = applyResourceChanges(
                currentResources = state.resources,
                resourceChanges = upgradeToBuy.resourceChanges
            )

            val updatedRatios = applyRatioChanges(
                currentRatios = state.ratios,
                ratioChanges = boughtUpgrade.ratioChanges,
                tags = state.player.tags,
            )

            return state.copy(
                selectables = state.selectables.map { selectable ->
                    updatedUpgrades[selectable.id] ?: selectable
                },
                resources = updatedResources,
                ratios = updatedRatios,
                player = state.player.copy(
                    generalTags = updateTags(state.player.generalTags, boughtUpgrade.tags)
                )
            ).addPlotEntry(boughtUpgrade)
        }

        else -> state
    }
}

private fun updateTags(
    currentTags: List<Tag>,
    selectedTagRelations: TagRelations,
): List<Tag> {
    return currentTags + selectedTagRelations[TagRelation.Provides].orEmpty() - selectedTagRelations[TagRelation.Removes].orEmpty()
        .toSet()
}

fun handleSelectableClicked(
    state: GameState,
    viewAction: MainViewAction.SelectableClicked
): GameState {
    val clickedSelectable = state.selectables.find { it.id == viewAction.id }
    return when (clickedSelectable) {
        is Action -> handleActionClicked(state, clickedSelectable)
        is Upgrade -> handleUpgradeSelected(state, clickedSelectable)
        is Location -> handleLocationSelected(state, clickedSelectable)
        else -> state
    }
}
