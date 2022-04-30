package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.core.ui.debugViewState
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.location.presentation.LocationModel
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel

fun mainViewState(
    resources: List<ResourceModel> = emptyList(),
    ratios: List<HumanityRatioModel> = emptyList(),
    actionState: ActionsState = actionsState(),
    shop: ShopState = shopState(),
    locations: List<LocationModel> = emptyList(),
    sectionCollapse: Map<SectionKey, Boolean> = mapOf(),
    drawerState: DrawerViewState = drawerViewState(),
) = MainViewState.Success(
    resources = resources,
    ratios = ratios,
    actionState = actionState,
    locations = locations,
    shop = shop,
    sectionCollapse = sectionCollapse,
    drawerState = drawerState,
)

fun actionsState(
    actionPanes: List<ActionPane> = emptyList(),
) = ActionsState(
    actionPanes = actionPanes,
)

fun shopState(
    upgradeLists: List<List<UpgradeModel>> = emptyList(),
) = ShopState(
    upgradeLists = upgradeLists,
)

fun drawerViewState(
    tabSelectorState: List<DrawerTab> = emptyList(),
    drawerContent: DrawerContentViewState = drawerDebugContent(),
) = DrawerViewState(
    tabSelectorState = tabSelectorState,
    drawerContent = drawerContent,
)

fun drawerDebugContent(
    jobSelection: List<PlayerJobModel> = emptyList(),
) = DrawerContentViewState.Debug(
    state = debugViewState(
        jobSelection = jobSelection,
    ),
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
