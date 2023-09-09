package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.RatioChanges
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuId
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsControl
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelations
import com.daniil.shevtsov.idle.feature.tagsystem.domain.provideTags
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import kotlinx.collections.immutable.toImmutableList

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
        MainViewAction.StartNewGameClicked -> handleStartNewGameClicked(state)
        is MainViewAction.MenuButtonClicked -> handleMenuButtonClicked(state, viewAction)
        is MainViewAction.SettingsSwitchUpdate -> handleSettingsSwitchUpdate(state, viewAction)
        is MainViewAction.SettingsTextSaved -> handleSettingsTextUpdate(state, viewAction)
    }
    return newState
}

fun handleStartNewGameClicked(state: GameState): GameState {
    return state.copy(currentScreen = Screen.GameStart, screenStack = emptyList())
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

fun handleMenuButtonClicked(
    state: GameState,
    viewAction: MainViewAction.MenuButtonClicked
): GameState {
    val newScreen = when (viewAction.id) {
        MenuId.StartGame -> Screen.GameStart
        MenuId.Settings -> Screen.Settings
    }
    return state.copy(
        currentScreen = newScreen,
        screenStack = state.screenStack + newScreen
    )
}

fun handleSettingsSwitchUpdate(
    state: GameState,
    viewAction: MainViewAction.SettingsSwitchUpdate
): GameState {
    val itemToUpdate = state.settings.settingsItems.find { it.key == viewAction.key }
    val switchToUpdate = itemToUpdate?.value as? SettingsControl.BooleanValue

    val newState = when {
        itemToUpdate != null && switchToUpdate != null -> {
            state.copy(
                settings = state.settings.copy(
                    settingsItems = state.settings.settingsItems.map { settingsItem ->
                        val newItem = when (settingsItem.key) {
                            itemToUpdate.key -> settingsItem.copy(
                                value = switchToUpdate.copy(isEnabled = !switchToUpdate.isEnabled)
                            )

                            else -> settingsItem
                        }
                        newItem
                    }.toImmutableList()
                )
            )
        }

        else -> state
    }

    return newState
}

fun handleSettingsTextUpdate(
    state: GameState,
    viewAction: MainViewAction.SettingsTextSaved
): GameState {
    val itemToUpdate = state.settings.settingsItems.find { it.key == viewAction.key }
    val textToUpdate = itemToUpdate?.value as? SettingsControl.StringValue

    val newState = when {
        itemToUpdate != null && textToUpdate != null -> {
            state.copy(
                settings = state.settings.copy(
                    settingsItems = state.settings.settingsItems.map { settingsItem ->
                        val newItem = when (settingsItem.key) {
                            itemToUpdate.key -> settingsItem.copy(
                                value = textToUpdate.copy(text = viewAction.newText)
                            )

                            else -> settingsItem
                        }
                        newItem
                    }.toImmutableList()
                )
            )
        }

        else -> state
    }

    return newState
}

fun handleLocationSelected(
    state: GameState,
    selectedLocation: Location
): GameState {
    val updatedTags = updateTags(state.player.generalTags, selectedLocation.tagRelations)
    val oldLocationTags =
        state.locationSelectionState.selectedLocation.tagRelations.provideTags.toSet()
    val finalTags = updatedTags - oldLocationTags
    return state.copy(
        locationSelectionState = state.locationSelectionState.copy(
            selectedLocation = selectedLocation,
        ),
        player = state.player.copy(
            generalTags = finalTags
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
    val tagRelations: TagRelations

    fun copy(
        id: Long? = null,
        title: String? = null,
        tagRelations: TagRelations? = null,
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

private fun List<Ratio>.suspicion() = find { it.key == RatioKey.Suspicion }?.value ?: 0.0

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

    var updatedRatios = applyRatioChanges(
        currentRatios = state.ratios,
        ratioChanges = selectedAction.ratioChanges,
        tags = state.player.tags,
        mainRatiokey = state.player.mainRatioKey,
    )

    return if (!hasInvalidChanges) {
        state.copy(
            ratios = updatedRatios,
            resources = updatedResources,
            player = state.player.copy(
                generalTags = updateTags(state.player.generalTags, selectedAction.tagRelations)
            ),
        ).let { state ->
            val loseRatio = RatioKey.Suspicion
            val winRatio = state.player.mainRatioKey
            val completedRatios = updatedRatios.filter { it.value >= 1.0 }.map(Ratio::key)
            when {
                completedRatios.isNotEmpty() -> state.copy(
                    currentEndingId = when {
                        completedRatios.contains(winRatio) -> 1L
                        completedRatios.contains(loseRatio) -> 0L
                        else -> null
                    },
                    currentScreen = Screen.FinishedGame,
                    screenStack = emptyList(),
                )

                else -> state
            }
        }
            .addPlotEntry(selectedAction)
            .let { newState -> finishTurn(oldState = state, newState = newState) }
    } else {
        state
    }
}

private fun applyRatioChanges(
    currentRatios: List<Ratio>,
    ratioChanges: RatioChanges,
    tags: List<Tag>,
    mainRatiokey: RatioKey,
): List<Ratio> {
    val ratioChanges = ratioChanges.mapKeys { (key, _) ->
        when (key) {
            RatioKey.MainRatio -> mainRatiokey
            else -> key
        }
    }
    return currentRatios.map { ratio ->

        val ratioChange = ratioChanges[ratio.key]
            ?.minByOrNull { (matchedTags, _) ->
                (tags - matchedTags.toSet()).size
            }?.value
        when (ratioChange) {
            null -> ratio
            else -> ratio.copy(value = ratio.value + ratioChange).let { ratio ->
                when {
                    ratio.value < 0.0 -> ratio.copy(value = 0.0)
                    else -> ratio
                }
            }
        }
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
    val resourceChanges = upgradeToBuy.resourceChanges.mapKeys { (key, _) ->
        when (key) {
            ResourceKey.MainResource -> state.player.mainResourceKey
            else -> key
        }
    }
    val hasInvalidChanges = hasInvalidChanges(
        currentResources = state.resources,
        resourceChanges = resourceChanges,
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
                resourceChanges = resourceChanges
            )

            val updatedRatios = applyRatioChanges(
                currentRatios = state.ratios,
                ratioChanges = boughtUpgrade.ratioChanges,
                tags = state.player.tags,
                mainRatiokey = state.player.mainRatioKey,
            )

            return state.copy(
                selectables = state.selectables.map { selectable ->
                    updatedUpgrades[selectable.id] ?: selectable
                },
                resources = updatedResources,
                ratios = updatedRatios,
                player = state.player.copy(
                    generalTags = updateTags(state.player.generalTags, boughtUpgrade.tagRelations)
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
    return when (val clickedSelectable = state.selectables.find { it.id == viewAction.id }) {
        is Upgrade -> handleUpgradeSelected(state, clickedSelectable)
        is Action -> handleActionClicked(state, clickedSelectable)
        is Location -> handleLocationSelected(state, clickedSelectable)
        else -> state
    }
}

private fun finishTurn(
    oldState: GameState,
    newState: GameState,
): GameState {
    var updatedRatios = newState.ratios
    var updatedResources = newState.resources
    if (updatedRatios.suspicion() > 0.54) {
        if (updatedResources.none { it.key == ResourceKey.Detective && it.value > 0.0 }) {
            updatedResources = updatedResources.map { resource ->
                when (resource.key) {
                    ResourceKey.Detective -> resource.copy(value = resource.value + 1.0)
                    else -> resource
                }
            }
        }
    }

    val detectives = oldState.resources.find { it.key == ResourceKey.Detective && it.value > 0.0 }
    if (detectives != null) {
        updatedRatios = updatedRatios.map { ratio ->
            when (ratio.key) {
                RatioKey.Suspicion -> ratio.copy(value = ratio.value + oldState.balanceConfig.detectiveSuspicionMultiplier * detectives.value)
                else -> ratio
            }
        }
    }

    return newState.copy(
        currentTurn = newState.currentTurn.copy(count = newState.currentTurn.count + 1),
        resources = updatedResources,
        ratios = updatedRatios,
    )
}
