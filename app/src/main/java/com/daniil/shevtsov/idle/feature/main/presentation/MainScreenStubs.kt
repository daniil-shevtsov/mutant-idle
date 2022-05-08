package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.location.presentation.locationSelectionViewState
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.UpgradesViewState
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel

fun mainViewState(
    resources: List<ResourceModel> = emptyList(),
    ratios: List<RatioModel> = emptyList(),
    actionState: ActionsState = actionsState(),
    upgradeState: UpgradesViewState = upgradeViewState(),
    locationSelectionViewState: LocationSelectionViewState = locationSelectionViewState(),
    sectionCollapse: Map<SectionKey, Boolean> = mapOf(),
) = MainViewState.Success(
    resources = resources,
    ratios = ratios,
    actionState = actionState,
    locationSelectionViewState = locationSelectionViewState,
    shop = upgradeState,
    sectionCollapse = sectionCollapse,
)

fun actionsState(
    actionPanes: List<ActionPane> = emptyList(),
) = ActionsState(
    actionPanes = actionPanes,
)

fun upgradeViewState(
    upgrades: List<UpgradeModel> = emptyList(),
) = UpgradesViewState(
    upgrades = upgrades,
)

fun actionPane(
    actions: List<ActionModel> = emptyList(),
) = ActionPane(
    actions = actions,
)

fun sectionState(
    key: SectionKey = SectionKey.Resources,
    isCollapsed: Boolean = false,
) = SectionState(
    key = key,
    isCollapsed = isCollapsed
)
