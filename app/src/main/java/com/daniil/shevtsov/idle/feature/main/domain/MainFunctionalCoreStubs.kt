package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.domain.balanceConfig
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.flavor.Flavor
import com.daniil.shevtsov.idle.feature.location.domain.LocationSelectionState
import com.daniil.shevtsov.idle.feature.location.domain.locationSelectionState
import com.daniil.shevtsov.idle.feature.main.presentation.SectionState
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.PlayerSpecies
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade

fun mainFunctionalCoreState(
    balanceConfig: BalanceConfig = balanceConfig(),
    resources: List<Resource> = emptyList(),
    ratios: List<Ratio> = emptyList(),
    upgrades: List<Upgrade> = emptyList(),
    actions: List<Action> = emptyList(),
    drawerTabs: List<DrawerTab> = emptyList(),
    sections: List<SectionState> = emptyList(),
    availableJobs: List<PlayerJob> = emptyList(),
    availableSpecies: List<PlayerSpecies> = emptyList(),
    locationSelectionState: LocationSelectionState = locationSelectionState(),
    flavors: List<Flavor> = emptyList(),
    player: Player = player(),
    currentScreen: Screen = Screen.Main,
) = gameState(
    balanceConfig = balanceConfig,
    resources = resources,
    ratios = ratios,
    upgrades = upgrades,
    actions = actions,
    drawerTabs = drawerTabs,
    sections = sections,
    availableJobs = availableJobs,
    availableSpecies = availableSpecies,
    locationSelectionState = locationSelectionState,
    flavors = flavors,
    player = player,
    currentScreen = currentScreen,
)
