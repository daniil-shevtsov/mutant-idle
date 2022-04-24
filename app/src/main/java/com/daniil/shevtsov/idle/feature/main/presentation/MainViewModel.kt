package com.daniil.shevtsov.idle.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.ActionType
import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.idle.feature.player.info.presentation.PlayerInfoState
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModelMapper
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModelMapper
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()

    private val temporaryHackyActionFlow =
        MutableSharedFlow<MainViewAction>()

    init {
        temporaryHackyActionFlow
            .onEach { viewAction ->
                val newFunctionalCoreState = mainFunctionalCore(
                    state = imperativeShell.getState(),
                    viewAction = viewAction
                )

                updateImperativeShell(newState = newFunctionalCoreState)
            }
            .launchIn(viewModelScope)


        imperativeShell.observeState()
            .map { state -> createMainViewState(state) }
            .onEach { viewState ->
                _state.value = viewState
            }
            .launchIn(viewModelScope)
    }

    fun handleAction(action: MainViewAction) {
        viewModelScope.launch {
            temporaryHackyActionFlow.emit(action)
        }
    }

    private fun updateImperativeShell(newState: MainFunctionalCoreState) {
        imperativeShell.updateState(newState)
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
            actionState = createActionState(state.actions, state.resources),
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
            sectionCollapse = state.sections.map { it.key to it.isCollapsed }.toMap(),
            drawerState = DrawerViewState(
                tabSelectorState = state.drawerTabs,
                drawerContent = when (state.drawerTabs.find { it.isSelected }?.id
                    ?: DrawerTabId.PlayerInfo) {
                    DrawerTabId.Debug -> {
                        DrawerContentViewState.Debug(
                            state = DebugViewState(
                                jobSelection = state.availableJobs.map { job ->
                                    with(job) {
                                        PlayerJobModel(
                                            id = id,
                                            title = title,
                                            tags = tags,
                                            isSelected = state.player.job.id == job.id,
                                        )
                                    }
                                },
                            )
                        )
                    }
                    DrawerTabId.PlayerInfo -> {
                        DrawerContentViewState.PlayerInfo(
                            playerInfo = PlayerInfoState(
                                playerJob = state.player.job,
                                playerTags = state.player.tags,
                            )
                        )
                    }
                }

            ),
        )
    }

    private fun createActionState(
        actions: List<Action>,
        resources: List<Resource>,
    ): ActionsState {
        return ActionsState(
            humanActionPane = ActionPane(
                actions = actions.filter { it.actionType == ActionType.Human }
                    .prepareActionForDisplay(resources = resources)
            ),
            mutantActionPane = ActionPane(
                actions = actions.filter { it.actionType == ActionType.Mutant }
                    .prepareActionForDisplay(resources = resources)
            )
        )
    }

    private fun List<Action>.prepareActionForDisplay(resources: List<Resource>) =
        map { it.toModel(resources) }.sortedByDescending { it.isEnabled }

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

    private fun initViewState(): MainViewState = MainViewState.Loading

    private fun Upgrade.mapStatus(resource: Double): UpgradeStatusModel {
        val statusModel = when {
            status == UpgradeStatus.Bought -> UpgradeStatusModel.Bought
            price.value <= resource -> UpgradeStatusModel.Affordable
            else -> UpgradeStatusModel.NotAffordable
        }
        return statusModel
    }

    private fun Action.toModel(resources: List<Resource>): ActionModel {
        val isActive = resourceChanges.all { (resourceKey, resourceChange) ->
            val currentResource = resources.find { it.key == resourceKey }!!.value
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

}
