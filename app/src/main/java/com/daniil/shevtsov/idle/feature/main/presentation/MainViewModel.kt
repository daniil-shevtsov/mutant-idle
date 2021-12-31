package com.daniil.shevtsov.idle.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.action.data.ActionsStorage
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceBehavior
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
    private val resourceStorage: ResourceStorage,
    private val mutantRatioStorage: MutantRatioStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()

    init {
        combine(
            ResourceBehavior.observeResource(resourceStorage),
            mutantRatioStorage.observeChange(),
            UpgradeBehavior.observeAll(upgradeStorage),
        ) { resource: Resource, mutantRatio: Double, upgrades: List<Upgrade> ->
            val newViewState = MainViewState.Success(
                resources = listOf(
                    ResourceModelMapper.map(
                        resource = resource,
                        name = "Blood",
                    )
                ),
                ratio = HumanityRatioModel(
                    name = getNameForRatio(mutantRatio),
                    percent = mutantRatio
                ),
                actionState = createActionState(),
                shop = upgrades
                    .map { upgrade ->
                        UpgradeModelMapper.map(
                            upgrade = upgrade,
                            status = upgrade.mapStatus(resource.value)
                        )
                    }
                    .sortedBy {
                        when (it.status) {
                            UpgradeStatusModel.Affordable -> 0
                            UpgradeStatusModel.NotAffordable -> 1
                            UpgradeStatusModel.Bought -> 2
                        }
                    }
                    .let { ShopState(upgradeLists = listOf(it)) }
            )
            newViewState
        }
            .onEach { viewState ->
                _state.value = viewState
            }
            .launchIn(viewModelScope)
    }

    private fun getNameForRatio(mutantRatio: Double): String {
        val name =  when {
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
        }
    }

    private fun handleUpgradeSelected(action: MainViewAction.UpgradeSelected) {
        viewModelScope.launch {
            CompositePurchaseBehavior.buyUpgrade(
                balanceConfig = balanceConfig,
                upgradeStorage = upgradeStorage,
                resourceStorage = resourceStorage,
                mutantRatioStorage = mutantRatioStorage,
                upgradeId = action.id,
            )
        }
    }

    private fun handleActionClicked(action: MainViewAction.ActionClicked) {
        TODO("Not yet implemented")
    }

    private fun initViewState(): MainViewState = MainViewState.Loading

    private fun createActionState() = ActionsState(
        actionPanes = listOf(
            ActionPane(
                actions = listOf(
                    ActionModel(id = 0L, title = "Work", subtitle = "The sun is high"),
                    ActionModel(id = 1L, title = "Buy a pet", subtitle = "It's so cute"),
                    ActionModel(id = 2L, title = "Eat food", subtitle = "It's not much"),
                    ActionModel(id = 3L, title = "Buy Groceries", subtitle = "It's a short walk"),
                    ActionModel(
                        id = 4L,
                        title = "Order Groceries",
                        subtitle = "I can hide at home"
                    ),
                )
            ),
            ActionPane(
                actions = listOf(
                    ActionModel(id = 100L, title = "Grow", subtitle = "Cultivating mass"),
                    ActionModel(id = 101L, title = "Eat a pet", subtitle = "Its time is up"),
                    ActionModel(
                        id = 104L,
                        title = "Hunt for rats",
                        subtitle = "Surely there are some"
                    ),
                    ActionModel(
                        id = 102L,
                        title = "Capture a person",
                        subtitle = "I think I can do it if I grow enough"
                    ),
                    ActionModel(
                        id = 103L,
                        title = "Eat captured person",
                        subtitle = "Finally some good fucking food"
                    ),
                )
            ),
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

}
