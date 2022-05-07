package com.daniil.shevtsov.idle.feature.coreshell.domain

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.domain.balanceConfig
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.flavor.Flavor
import com.daniil.shevtsov.idle.feature.gamefinish.domain.Ending
import com.daniil.shevtsov.idle.feature.location.domain.LocationSelectionState
import com.daniil.shevtsov.idle.feature.location.domain.locationSelectionState
import com.daniil.shevtsov.idle.feature.main.presentation.SectionState
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.PlayerSpecies
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.unlocks.domain.UnlockState
import com.daniil.shevtsov.idle.feature.unlocks.domain.unlockState
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade

data class GameState(
    val balanceConfig: BalanceConfig,
    val resources: List<Resource>,
    val ratios: List<Ratio>,
    val upgrades: List<Upgrade>,
    val actions: List<Action>,
    val sections: List<SectionState>,
    val drawerTabs: List<DrawerTab>,
    val availableJobs: List<PlayerJob>,
    val availableSpecies: List<PlayerSpecies>,
    val availableEndings: List<Ending>,
    val locationSelectionState: LocationSelectionState,
    val flavors: List<Flavor>,
    val player: Player,
    val currentScreen: Screen,
    val screenStack: List<Screen>,
    val unlockState: UnlockState,
)

fun gameState(
    balanceConfig: BalanceConfig = balanceConfig(),
    resources: List<Resource> = emptyList(),
    ratios: List<Ratio> = emptyList(),
    upgrades: List<Upgrade> = emptyList(),
    actions: List<Action> = emptyList(),
    drawerTabs: List<DrawerTab> = emptyList(),
    sections: List<SectionState> = emptyList(),
    availableJobs: List<PlayerJob> = emptyList(),
    availableSpecies: List<PlayerSpecies> = emptyList(),
    availableEndings: List<Ending> = emptyList(),
    locationSelectionState: LocationSelectionState = locationSelectionState(),
    flavors: List<Flavor> = emptyList(),
    player: Player = player(),
    currentScreen: Screen = Screen.Main,
    screenStack: List<Screen> = emptyList(),
    unlockState: UnlockState = unlockState(),
) = GameState(
    balanceConfig = balanceConfig,
    resources = resources,
    ratios = ratios,
    upgrades = upgrades,
    actions = actions,
    drawerTabs = drawerTabs,
    sections = sections,
    availableJobs = availableJobs,
    availableSpecies = availableSpecies,
    availableEndings = availableEndings,
    locationSelectionState = locationSelectionState,
    flavors = flavors,
    player = player,
    currentScreen = currentScreen,
    screenStack = screenStack,
    unlockState = unlockState,
)
