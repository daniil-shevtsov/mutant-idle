package com.daniil.shevtsov.idle.feature.coreshell.domain

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.domain.balanceConfig
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.flavor.Flavor
import com.daniil.shevtsov.idle.feature.gamefinish.domain.Ending
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.location.domain.LocationSelectionState
import com.daniil.shevtsov.idle.feature.location.domain.locationSelectionState
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.main.presentation.SectionState
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.unlocks.domain.UnlockState
import com.daniil.shevtsov.idle.feature.unlocks.domain.unlockState
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade

data class GameState(
    val balanceConfig: BalanceConfig,
    val selectables: List<Selectable>,
    val resources: List<Resource>,
    val ratios: List<Ratio>,
    val plotEntries: List<PlotEntry>,
    val sections: List<SectionState>,
    val drawerTabs: List<DrawerTab>,
    val availableTraits: List<PlayerTrait>,
    val locationSelectionState: LocationSelectionState,
    val flavors: List<Flavor>,
    val player: Player,
    val currentScreen: Screen,
    val screenStack: List<Screen>,
    val unlockState: UnlockState,
    val allEndings: List<Ending>,
    val currentEndingId: Long?,
) {
    val locations: List<Location>
        get() = selectables.filterIsInstance<Location>()
    val upgrades: List<Upgrade>
        get() = selectables.filterIsInstance<Upgrade>()
    val actions: List<Action>
        get() = selectables.filterIsInstance<Action>()
}

fun gameState(
    balanceConfig: BalanceConfig = balanceConfig(),
    selectables: List<Selectable> = emptyList(),
    actions: List<Action> = emptyList(),
    upgrades: List<Upgrade> = emptyList(),
    locations: List<Location> = emptyList(),
    resources: List<Resource> = emptyList(),
    ratios: List<Ratio> = emptyList(),
    plotEntries: List<PlotEntry> = emptyList(),
    drawerTabs: List<DrawerTab> = emptyList(),
    sections: List<SectionState> = emptyList(),
    availableTraits: List<PlayerTrait> = emptyList(),
    locationSelectionState: LocationSelectionState = locationSelectionState(),
    flavors: List<Flavor> = emptyList(),
    player: Player = player(),
    currentScreen: Screen = Screen.Main,
    screenStack: List<Screen> = emptyList(),
    unlockState: UnlockState = unlockState(),
    allEndings: List<Ending> = emptyList(),
    currentEndingId: Long? = null,
) = GameState(
    balanceConfig = balanceConfig,
    selectables = (selectables + locations + upgrades + actions).distinct(),
    resources = resources,
    ratios = ratios,
    plotEntries = plotEntries,
    drawerTabs = drawerTabs,
    sections = sections,
    availableTraits = availableTraits,
    locationSelectionState = locationSelectionState,
    flavors = flavors,
    player = player,
    currentScreen = currentScreen,
    screenStack = screenStack,
    unlockState = unlockState,
    allEndings = allEndings,
    currentEndingId = currentEndingId,
)
