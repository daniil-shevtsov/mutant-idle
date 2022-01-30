package com.daniil.shevtsov.idle.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.action.data.ActionsStorage
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.ActionBehavior
import com.daniil.shevtsov.idle.feature.action.domain.ActionType
import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.ratio.data.RatiosStorage
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceBehavior
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModelMapper
import com.daniil.shevtsov.idle.feature.shop.domain.CompositePurchaseBehavior
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeBehavior
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
    private val mutantRatioStorage: MutantRatioStorage,
    private val ratiosStorage: RatiosStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()

    private val sectionCollapseState = MutableStateFlow<Map<SectionKey, Boolean>>(
        mapOf(
            SectionKey.Resources to false,
            SectionKey.Actions to false,
            SectionKey.Upgrades to false,
        )
    )

    init {
        combine(
            ResourceBehavior.observeResource(
                resourcesStorage = resourcesStorage,
                key = ResourceKey.Blood,
            ),
            ResourceBehavior.observeAllResources(
                resourcesStorage = resourcesStorage,
            ),
            ratiosStorage.observeAll(),
            UpgradeBehavior.observeAll(upgradeStorage),
            combine(ActionBehavior.observeAll(actionsStorage), sectionCollapseState, ::Pair),
        ) { blood: Resource,
            resources: List<Resource>,
            ratios: List<Ratio>,
            upgrades: List<Upgrade>,
            //actions: List<Action> ->
            actionsAndSectionState: Pair<List<Action>, Map<SectionKey, Boolean>> ->
            val actions = actionsAndSectionState.first
            val sectionState = actionsAndSectionState.second

            val newViewState = MainViewState.Success(
                resources = resources.map { resource ->
                    ResourceModelMapper.map(
                        resource = resource,
                        name = resource.name,
                    )
                },
                ratios = ratios.map {
                    HumanityRatioModel(
                        name = getNameForRatio(it),
                        percent = it.value
                    )
                },
                actionState = actions.toActionState(),
                shop = upgrades
                    .map { upgrade ->
                        UpgradeModelMapper.map(
                            upgrade = upgrade,
                            status = upgrade.mapStatus(blood.value)
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
                sectionCollapse = sectionState,
            )
            newViewState
        }
            .onEach { viewState ->
                _state.value = viewState
            }
            .launchIn(viewModelScope)
    }

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

    fun handleAction(action: MainViewAction) {
        when (action) {
            is MainViewAction.UpgradeSelected -> handleUpgradeSelected(action)
            is MainViewAction.ActionClicked -> handleActionClicked(action)
            is MainViewAction.ToggleSectionCollapse -> toggleSectionCollapse(action)
        }
    }

    private fun handleUpgradeSelected(action: MainViewAction.UpgradeSelected) {
        viewModelScope.launch {
            CompositePurchaseBehavior.buyUpgrade(
                balanceConfig = balanceConfig,
                upgradeStorage = upgradeStorage,
                resourcesStorage = resourcesStorage,
                mutantRatioStorage = mutantRatioStorage,
                ratiosStorage = ratiosStorage,
                upgradeId = action.id,
            )
        }
    }

    private fun handleActionClicked(action: MainViewAction.ActionClicked) {
        viewModelScope.launch {
            val selectedAction = ActionBehavior.getById(actionsStorage, action.id)
            selectedAction?.resourceChanges?.forEach { (resourceKey, resourceValue) ->
                ResourceBehavior.applyResourceChange(
                    resourcesStorage = resourcesStorage,
                    resourceKey = resourceKey,
                    amount = resourceValue,
                )
            }
            selectedAction?.ratioChanges?.forEach { (key, value) ->
                val oldRatio = ratiosStorage.getByKey(key = key)!!.value
                ratiosStorage.updateByKey(key = key, newRatio = oldRatio + value)
            }
        }
    }

    private fun toggleSectionCollapse(action: MainViewAction.ToggleSectionCollapse) {
        val oldState = sectionCollapseState.value
        val newState = oldState.toMutableMap()
            .apply { put(action.key, !(oldState[action.key] ?: false)) }
            .toMap()
        sectionCollapseState.value = newState
    }

    private fun initViewState(): MainViewState = MainViewState.Loading

    private fun List<Action>.toActionState() = ActionsState(
        humanActionPane = ActionPane(
            actions = filter { it.actionType == ActionType.Human }.map { it.toModel() }
        ),
        mutantActionPane = ActionPane(
            actions = filter { it.actionType == ActionType.Mutant }.map { it.toModel() }
        )
    )

    private fun Upgrade.mapStatus(resource: Double): UpgradeStatusModel {
        val statusModel = when {
            status == UpgradeStatus.Bought -> UpgradeStatusModel.Bought
            price.value <= resource -> UpgradeStatusModel.Affordable
            else -> UpgradeStatusModel.NotAffordable
        }
        return statusModel
    }

    private fun Action.toModel() = ActionModel(
        id = id,
        title = title,
        subtitle = subtitle,
        icon = when (actionType) {
            ActionType.Human -> ActionIcon.Human
            ActionType.Mutant -> ActionIcon.Mutant
        }
    )

}
