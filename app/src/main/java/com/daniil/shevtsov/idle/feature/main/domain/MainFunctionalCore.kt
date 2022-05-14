package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
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
        is MainViewAction.ToggleSectionCollapse -> handleSectionCollapsed(
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

fun handleActionClicked(
    state: GameState,
    viewAction: MainViewAction.ActionClicked
): GameState {
    val selectedAction = state.actions.find { action -> action.id == viewAction.id }!!

    val hasInvalidChanges = hasInvalidChanges(
        currentResources = state.resources,
        resourceChanges = selectedAction.resourceChanges,
    )
    val updatedResources = applyResourceChanges(
        currentResources = state.resources,
        resourceChanges = selectedAction.resourceChanges
    )

    val updatedRatios = applyRatioChanges(state, selectedAction.ratioChanges)

    val newTags =
        state.player.generalTags + selectedAction.tags[TagRelation.Provides].orEmpty() - selectedAction.tags[TagRelation.Removes].orEmpty()

    return if (!hasInvalidChanges) {
        state.copy(
            ratios = updatedRatios,
            resources = updatedResources,
            player = state.player.copy(
                generalTags = newTags
            ),
            currentScreen = when {
                updatedRatios.find { it.key == RatioKey.Suspicion }?.value ?: 0.0 >= 1.0 -> Screen.FinishedGame
                else -> state.currentScreen
            }
        )
    } else {
        state
    }
}

private fun applyRatioChanges(
    state: GameState,
    ratioChanges: Map<RatioKey, Float>
): List<Ratio> = state.ratios.map { ratio ->
    when (val ratioChange = ratioChanges[ratio.key]) {
        null -> ratio
        else -> ratio.copy(value = ratio.value + ratioChange)
    }
}

private fun applyResourceChanges(
    currentResources: List<Resource>,
    resourceChanges: Map<ResourceKey, Double>
) = currentResources.map { resource ->
    when (val resourceChange = resourceChanges[resource.key]) {
        null -> resource
        else -> resource.copy(value = resource.value + resourceChange)
    }
}

private fun hasInvalidChanges(
    currentResources: List<Resource>,
    resourceChanges: Map<ResourceKey, Double>,
): Boolean = resourceChanges.any { (resourceKey, resourceChange) ->
    val currentResourceValue = currentResources
        .find { resource -> resource.key == resourceKey }!!.value

    currentResourceValue + resourceChange < 0
}

fun handleUpgradeSelected(
    state: GameState,
    viewAction: MainViewAction.UpgradeSelected
): GameState {
    val upgradeToBuy = state.upgrades.find { upgrade -> upgrade.id == viewAction.id }!!

    val currentResource = state.resources.find { resource -> resource.key == ResourceKey.Blood }!!

    val hasInvalidChanges = hasInvalidChanges(
        currentResources = state.resources,
        resourceChanges = upgradeToBuy.resourceChanges,
    )

    return when {
        !hasInvalidChanges -> {
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

            val updatedResources = applyResourceChanges(
                currentResources = state.resources,
                resourceChanges = upgradeToBuy.resourceChanges
            )

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
