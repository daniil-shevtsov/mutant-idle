package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus

fun mainFunctionalCore(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction,
): MainFunctionalCoreState {
    val newState = when (viewAction) {
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
        is MainViewAction.LocationSelected -> handleLocationSelected(
            state = state,
            viewAction = viewAction,
        )
    }
    return newState
}

fun handleLocationSelectionExpandChange(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.LocationSelectionExpandChange
): MainFunctionalCoreState {
    return state.copy(
        isLocationSelectionExpanded = !state.isLocationSelectionExpanded,
    )
}

fun handleLocationSelected(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.LocationSelected
): MainFunctionalCoreState {
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

fun handleSpeciesSelected(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.DebugSpeciesSelected
): MainFunctionalCoreState {
    val newSpecies = state.availableSpecies.find { it.id == viewAction.id }!!

    return state.copy(
        player = state.player.copy(
            species = newSpecies,
        )
    )
}

fun handleDrawerTabSwitched(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.DrawerTabSwitched
): MainFunctionalCoreState {
    return state.copy(
        drawerTabs = state.drawerTabs.map { tab ->
            tab.copy(isSelected = tab.id == viewAction.id)
        }
    )
}

fun handleSectionCollapsed(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.ToggleSectionCollapse,
): MainFunctionalCoreState {
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
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.ActionClicked
): MainFunctionalCoreState {
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
            )
        )
    } else {
        state
    }
}

fun handleUpgradeSelected(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.UpgradeSelected
): MainFunctionalCoreState {
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
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.DebugJobSelected
): MainFunctionalCoreState {
    val newJob = state.availableJobs.find { it.id == viewAction.id }!!

    return state.copy(
        player = state.player.copy(
            job = newJob,
        )
    )

}
