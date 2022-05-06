package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus

fun mainFunctionalCore(
    state: GameState,
    viewAction: MainViewAction,
): GameState {
    val newState = when (viewAction) {
        is MainViewAction.LocationSelected -> handleLocationSelected(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.ActionClicked -> handleActionClicked(
            state = state,
            viewAction = viewAction
        )
        is MainViewAction.UpgradeSelected -> handleUpgradeSelected(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.DebugJobSelected -> handleDebugJobSelected(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.DrawerTabSwitched -> handleDrawerTabSwitched(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.ToggleSectionCollapse -> handleSectionCollapsed(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.DebugSpeciesSelected -> handleSpeciesSelected(
            state = state,
            viewAction = viewAction,
        )
        is MainViewAction.LocationSelectionExpandChange -> handleLocationSelectionExpandChange(
            state = state,
            viewAction = viewAction,
        )
        MainViewAction.Init -> state
    }
    return newState
}

fun handleLocationSelectionExpandChange(
    state: GameState,
    viewAction: MainViewAction.LocationSelectionExpandChange
): GameState {
    return state.copy(
        locationSelectionState = state.locationSelectionState.copy(
            isSelectionExpanded = !state.locationSelectionState.isSelectionExpanded,
        ),
    )
}

fun handleLocationSelected(
    state: GameState,
    viewAction: MainViewAction.LocationSelected
): GameState {
    val selectedLocation = state.locationSelectionState.allLocations.find { it.id == viewAction.id }

    val oldTags = state.locationSelectionState.selectedLocation.tags[TagRelation.Provides].orEmpty()
    val newTags = selectedLocation?.tags?.get(TagRelation.Provides).orEmpty()

    return when {
        selectedLocation != null -> state.copy(
            locationSelectionState = state.locationSelectionState.copy(
                selectedLocation = selectedLocation,
            ),
            player = state.player.copy(
                generalTags = state.player.generalTags - oldTags + newTags
            )
        )
        else -> state
    }

}

private fun handleSpeciesSelected(
    state: GameState,
    viewAction: MainViewAction.DebugSpeciesSelected
): GameState {
    val newSpecies = state.availableSpecies.find { it.id == viewAction.id }!!

    return state.copy(
        player = state.player.copy(
            species = newSpecies,
        )
    )
}

fun handleDrawerTabSwitched(
    state: GameState,
    viewAction: MainViewAction.DrawerTabSwitched
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

fun handleActionClicked(
    state: GameState,
    viewAction: MainViewAction.ActionClicked
): GameState {
    val selectedAction = state.actions.find { action -> action.id == viewAction.id }!!

    val hasInvalidChanges = selectedAction.resourceChanges.any { (resourceKey, resourceChange) ->
        val currentResourceValue = state.resources
            .find { resource -> resource.key == resourceKey }!!.value

        currentResourceValue + resourceChange < 0
    }

    val updatedResources = state.resources.map { resource ->
        when (val resourceChange = selectedAction.resourceChanges[resource.key]) {
            null -> resource
            else -> resource.copy(value = resource.value + resourceChange)
        }
    }
    val updatedRatios = state.ratios.map { ratio ->
        when (val ratioChange = selectedAction.ratioChanges[ratio.key]) {
            null -> ratio
            else -> ratio.copy(value = ratio.value + ratioChange)
        }
    }

    val newTags =
        state.player.generalTags + selectedAction.tags[TagRelation.Provides].orEmpty() - selectedAction.tags[TagRelation.Removes].orEmpty()

    return if (!hasInvalidChanges) {
        state.copy(
            ratios = updatedRatios,
            resources = updatedResources,
            player = state.player.copy(
                generalTags = newTags
            ),
//            currentScreen = when {
//                updatedRatios.find { it.key == RatioKey.Suspicion }?.value ?: 0.0 >= 1.0 -> Screen.FinishedGame
//                else -> state.currentScreen
//            }
        )
    } else {
        state
    }
}

fun handleUpgradeSelected(
    state: GameState,
    viewAction: MainViewAction.UpgradeSelected
): GameState {
    val upgradeToBuy = state.upgrades.find { upgrade -> upgrade.id == viewAction.id }!!

    val currentResource = state.resources.find { resource -> resource.key == ResourceKey.Blood }!!

    return when {
        upgradeToBuy.price.value <= currentResource.value -> {
            val newStatus = when {
                upgradeToBuy.price.value <= currentResource.value -> UpgradeStatus.Bought
                else -> UpgradeStatus.NotBought
            }

            val boughtUpgrade = upgradeToBuy.copy(status = newStatus)

            val updatedUpgrades = state.upgrades.map { upgrade ->
                if (upgrade.id == boughtUpgrade.id) {
                    boughtUpgrade
                } else {
                    upgrade
                }
            }
            val resourceToUpdate =
                state.resources.find { resource -> resource.key == ResourceKey.Blood }!!

            val updatedResource = resourceToUpdate.copy(
                value = resourceToUpdate.value - upgradeToBuy.price.value
            )

            val updatedResources = state.resources.map { resource ->
                when (resource.key) {
                    updatedResource.key -> updatedResource
                    else -> resource
                }
            }

            val ratioToUpdate = state.ratios.find { ratio -> ratio.key == RatioKey.Mutanity }!!

            val upgradePercent =
                boughtUpgrade.price.value / state.balanceConfig.resourceSpentForFullMutant

            val updatedRatio = ratioToUpdate.copy(
                value = ratioToUpdate.value + upgradePercent
            )

            val updatedRatios = state.ratios.map { ratio ->
                when (ratio.key) {
                    updatedRatio.key -> updatedRatio
                    else -> ratio
                }
            }

            return state.copy(
                upgrades = updatedUpgrades,
                resources = updatedResources,
                ratios = updatedRatios,
                player = state.player.copy(
                    generalTags = state.player.generalTags + boughtUpgrade.tags[TagRelation.Provides].orEmpty()
                )
            )
        }
        else -> state
    }
}

fun handleDebugJobSelected(
    state: GameState,
    viewAction: MainViewAction.DebugJobSelected
): GameState {
    val newJob = state.availableJobs.find { it.id == viewAction.id }!!

    return state.copy(
        player = state.player.copy(
            job = newJob,
        )
    )

}
