package com.daniil.shevtsov.idle.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.main.data.resource.NewResourceBehavior
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.upgrade.UpgradeBehavior
import com.daniil.shevtsov.idle.main.data.upgrade.UpgradeStorage
import com.daniil.shevtsov.idle.main.domain.purchase.CompositePurchaseBehavior
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.actions.ActionModel
import com.daniil.shevtsov.idle.main.ui.actions.ActionPane
import com.daniil.shevtsov.idle.main.ui.actions.ActionsState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModelMapper
import com.daniil.shevtsov.idle.main.ui.shop.ShopState
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModelMapper
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeStatusModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val upgradeStorage: UpgradeStorage,
    private val resourceStorage: ResourceStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()

    init {
        NewResourceBehavior.observeResource(resourceStorage)
            .onEach { resource ->
                _state.value = MainViewState.Success(
                    resources = listOf(
                        ResourceModelMapper.map(
                            resource = resource,
                            name = "Blood",
                        )
                    ),
                    actionState = createActionState(),
                    shop = UpgradeBehavior.observeAll(storage = upgradeStorage)
                        .firstOrNull()
                        .orEmpty()
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
            }
            .launchIn(viewModelScope)
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
                upgradeStorage = upgradeStorage,
                resourceStorage = resourceStorage,
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
