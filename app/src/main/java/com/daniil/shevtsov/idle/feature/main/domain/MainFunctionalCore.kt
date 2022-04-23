package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
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
        else -> TODO("Not Implemented")
    }
    return newState
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

    return if (!hasInvalidChanges) {
        state.copy(
            ratios = updatedRatios,
            resources = updatedResources,
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
            )
        }
        else -> state
    }
}

fun handleDebugJobSelected(
    state: MainFunctionalCoreState,
    viewAction: MainViewAction.DebugJobSelected
): MainFunctionalCoreState {
    val previousPlayerTags = state.player.tags
    val previousJobTags = state.player.job.tags
    val newJobTags = viewAction.job.tags

    val newPlayerTags = previousPlayerTags - previousJobTags + newJobTags

    return state.copy(
        player = state.player.copy(
            job = viewAction.job,
            tags = newPlayerTags,
        )
    )

}
