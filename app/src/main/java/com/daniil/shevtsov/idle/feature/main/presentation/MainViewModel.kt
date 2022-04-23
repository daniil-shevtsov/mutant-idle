package com.daniil.shevtsov.idle.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.action.data.ActionsStorage
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.ActionType
import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.debug.data.DebugConfigStorage
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.idle.feature.player.core.data.PlayerStorage
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.ratio.data.RatiosStorage
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModelMapper
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModelMapper
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val balanceConfig: BalanceConfig,
    private val upgradeStorage: UpgradeStorage,
    private val actionsStorage: ActionsStorage,
    private val resourcesStorage: ResourcesStorage,
    private val ratiosStorage: RatiosStorage,
    private val debugConfigStorage: DebugConfigStorage,
    private val playerStorage: PlayerStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()

    private val temporaryHackyActionFlow =
        MutableSharedFlow<MainViewAction>()

    private val sectionCollapseState = MutableStateFlow(
        mapOf(
            SectionKey.Resources to false,
            SectionKey.Actions to false,
            SectionKey.Upgrades to false,
        )
    )

    init {
        temporaryHackyActionFlow
            .onEach { viewAction ->
                val resources = resourcesStorage.observeAll().first()
                val ratios = ratiosStorage.observeAll().first()
                val upgrades = upgradeStorage.observeAll().first()
                val actions = actionsStorage.observeAll().first()
                val sectionState = sectionCollapseState.first()
                val availableJobs = debugConfigStorage.observeAvailableJobs().first()
                val player = playerStorage.observe().first()

                val functionalCoreState = MainFunctionalCoreState(
                    player = player,
                    balanceConfig = balanceConfig,
                    resources = resources,
                    ratios = ratios,
                    upgrades = upgrades,
                    actions = actions,
                    sectionState = sectionState,
                    availableJobs = availableJobs,
                )

                val newFunctionalCoreState = mainFunctionalCore(
                    state = functionalCoreState,
                    viewAction = viewAction
                )

                updateImperativeShell(newState = newFunctionalCoreState)
            }
            .launchIn(viewModelScope)


        combine(
            resourcesStorage.observeAll(),
            ratiosStorage.observeAll(),
            upgradeStorage.observeAll(),
            actionsStorage.observeAll(),
            sectionCollapseState,
            debugConfigStorage.observeAvailableJobs(),
            playerStorage.observe(),
        ) { resources: List<Resource>,
            ratios: List<Ratio>,
            upgrades: List<Upgrade>,
            actions: List<Action>,
            sectionState: Map<SectionKey, Boolean>,
            availableJobs: List<PlayerJob>,
            player: Player ->

            val newState = MainFunctionalCoreState(
                player = player,
                balanceConfig = balanceConfig,
                resources = resources,
                ratios = ratios,
                upgrades = upgrades,
                actions = actions,
                sectionState = sectionState,
                availableJobs = availableJobs,
            )

            createMainViewState(newState)
        }
            .onEach { viewState ->
                _state.value = viewState
            }
            .launchIn(viewModelScope)
    }

    fun handleAction(action: MainViewAction) {
        when (action) {
            is MainViewAction.UpgradeSelected -> handleUpgradeSelected(action)
            is MainViewAction.ActionClicked -> handleActionClicked(action)
            is MainViewAction.ToggleSectionCollapse -> toggleSectionCollapse(action)
            is MainViewAction.DebugJobSelected -> handleJobSelected(action)
        }
    }

    private fun updateImperativeShell(newState: MainFunctionalCoreState) {
        playerStorage.update(newPlayer = newState.player)
        upgradeStorage.updateALl(newUpgrades = newState.upgrades)
        resourcesStorage.upgradeAll(newResources = newState.resources)
        ratiosStorage.upgradeAll(newRatios = newState.ratios)
        actionsStorage.upgradeAll(newActions = newState.actions)
        sectionCollapseState.value = newState.sectionState
        debugConfigStorage.updateAvailableJobs(newAvailableJobs = newState.availableJobs)
    }

    private fun createMainViewState(state: MainFunctionalCoreState): MainViewState {
        return MainViewState.Success(
            resources = state.resources.map { resource ->
                ResourceModelMapper.map(
                    resource = resource,
                    name = resource.name,
                )
            },
            ratios = state.ratios.map {
                HumanityRatioModel(
                    name = getNameForRatio(it),
                    percent = it.value
                )
            },
            actionState = createActionState(state.actions),
            shop = state.upgrades
                .map { upgrade ->
                    UpgradeModelMapper.map(
                        upgrade = upgrade,
                        status = upgrade.mapStatus(
                            state.resources.find { it.key == ResourceKey.Blood }?.value ?: 0.0
                        )
                    )
                }
                .sortedBy {
                    when (it.status) {
                        UpgradeStatusModel.Affordable -> 0
                        UpgradeStatusModel.NotAffordable -> 1
                        UpgradeStatusModel.Bought -> 2
                    }
                }
                .let { ShopState(upgradeLists = listOf(it)) },
            sectionCollapse = state.sectionState,
            drawerState = DrawerViewState(
                tabSelectorState = emptyList(),
                drawerContent = DrawerContentViewState.Debug(
                    state = DebugViewState(
                        jobSelection = state.availableJobs,
                    )
                )
            ),
        )
    }

    private fun createActionState(actions: List<Action>): ActionsState {
        return ActionsState(
            humanActionPane = ActionPane(
                actions = actions.filter { it.actionType == ActionType.Human }
                    .prepareActionForDisplay()
            ),
            mutantActionPane = ActionPane(
                actions = actions.filter { it.actionType == ActionType.Mutant }
                    .prepareActionForDisplay()
            )
        )
    }

    private fun List<Action>.prepareActionForDisplay() =
        map { it.toModel() }.sortedByDescending { it.isEnabled }

    private fun getNameForRatio(ratio: Ratio) = when (ratio.key) {
        RatioKey.Mutanity -> getMutanityNameForRatio(ratio.value)
        RatioKey.Suspicion -> getSuspicionNameForRatio(ratio.value)
    }

    private fun getSuspicionNameForRatio(
        value: Double
    ): String = when {
        value < 0.15f -> "Unknown"
        value < 0.25f -> "Rumors"
        value < 0.50f -> "News"
        value < 0.80f -> "Investigation"
        else -> "Manhunt"
    }

    private fun getMutanityNameForRatio(mutantRatio: Double): String {
        val name = when {
            mutantRatio < 0.15 -> "Human"
            mutantRatio < 0.25 -> "Dormant"
            mutantRatio < 0.50 -> "Hidden"
            mutantRatio < 0.80 -> "Covert"
            else -> "Honest"
        }
        return name
    }

    private fun handleUpgradeSelected(action: MainViewAction.UpgradeSelected) {
        viewModelScope.launch {
            temporaryHackyActionFlow.emit(action)
        }
    }

    private fun handleActionClicked(action: MainViewAction.ActionClicked) {
        viewModelScope.launch {
            temporaryHackyActionFlow.emit(action)
        }
    }

    private fun toggleSectionCollapse(action: MainViewAction.ToggleSectionCollapse) {
        val oldState = sectionCollapseState.value
        val newState = oldState.toMutableMap()
            .apply { put(action.key, !(oldState[action.key] ?: false)) }
            .toMap()
        sectionCollapseState.value = newState
    }

    private fun handleJobSelected(action: MainViewAction.DebugJobSelected) {
        viewModelScope.launch {
            temporaryHackyActionFlow.emit(action)
        }
    }

    private fun initViewState(): MainViewState = MainViewState.Loading

    private fun Upgrade.mapStatus(resource: Double): UpgradeStatusModel {
        val statusModel = when {
            status == UpgradeStatus.Bought -> UpgradeStatusModel.Bought
            price.value <= resource -> UpgradeStatusModel.Affordable
            else -> UpgradeStatusModel.NotAffordable
        }
        return statusModel
    }

    private fun Action.toModel(): ActionModel {
        val isActive = resourceChanges.all { (resourceKey, resourceChange) ->
            val currentResource = resourcesStorage.getByKey(resourceKey)!!.value
            currentResource + resourceChange >= 0
        }

        return ActionModel(
            id = id,
            title = title,
            subtitle = subtitle,
            icon = when (actionType) {
                ActionType.Human -> ActionIcon.Human
                ActionType.Mutant -> ActionIcon.Mutant
            },
            isEnabled = isActive,
        )
    }

    private fun <T0, T1, T2, T3, T4, T5, T6, /*T7,*/ R> combine(
        flow0: Flow<T0>,
        flow1: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        flow6: Flow<T6>,
//        flow7: Flow<T7>,
        transform: suspend (
            T0,
            T1,
            T2,
            T3,
            T4,
            T5,
            T6,
//            T7,
        ) -> R
    ): Flow<R> = combine(
        flow0,
        flow1,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
//        flow7,
    ) { args: Array<*> ->
        transform(
            args[0] as T0,
            args[1] as T1,
            args[2] as T2,
            args[3] as T3,
            args[4] as T4,
            args[5] as T5,
            args[6] as T6,
//            args[7] as T7,
        )
    }

}
