package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.location.presentation.locationSelectionViewState
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.UpgradesViewState
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel

fun mainViewState(
    resources: List<ResourceModel> = emptyList(),
    ratios: List<RatioModel> = emptyList(),
    actionState: ActionsState = actionsState(),
    plotEntries: List<PlotEntry> = emptyList(),
    upgradeState: UpgradesViewState = upgradeViewState(),
    locationSelectionViewState: LocationSelectionViewState = locationSelectionViewState(),
    sectionCollapse: Map<SectionKey, Boolean> = mapOf(),
    turnCount: Int = 0,
) = MainViewState.Success(
    resources = resources,
    ratios = ratios,
    plotEntries = plotEntries,
    actionState = actionState,
    locationSelectionViewState = locationSelectionViewState,
    shop = upgradeState,
    sectionCollapse = sectionCollapse,
    turnCount = turnCount,
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
