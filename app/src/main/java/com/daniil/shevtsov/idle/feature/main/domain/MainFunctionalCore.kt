package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus

fun mainFunctionalCore(
    state: MainFunctionalCoreState,
    action: MainViewAction,
): MainFunctionalCoreState {
    val newState = when (action) {
        is MainViewAction.UpgradeSelected -> handleUpgradeSelected(
            state = state,
            action = action,
        )
        is MainViewAction.DebugJobSelected -> handleDebugJobSelected(
            state = state,
            action = action,
        )
        else -> TODO("Not Implemented")
    }
    return newState
}

fun handleUpgradeSelected(
    state: MainFunctionalCoreState,
    action: MainViewAction.UpgradeSelected
): MainFunctionalCoreState {
    val upgradeToBuy = state.upgrades.find { upgrade -> upgrade.id == action.id }!!

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
    action: MainViewAction.DebugJobSelected
): MainFunctionalCoreState {
    val previousPlayerTags = state.player.tags
    val previousJobTags = state.player.job.tags
    val newJobTags = action.job.tags
    
    val newPlayerTags = previousPlayerTags - previousJobTags + newJobTags

    return state.copy(
        player = state.player.copy(
            job = action.job,
            tags = newPlayerTags,
        )
    )

}
