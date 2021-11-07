package com.daniil.shevtsov.idle.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.BuyUpgradeUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.ObserveUpgradesUseCase
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
    private val observeResource: ObserveResourceUseCase,
    private val observeUpgrades: ObserveUpgradesUseCase,
    private val buyUpgrade: BuyUpgradeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()

    init {
        observeResource()
            .onEach { resource ->
                _state.value = MainViewState.Success(
                    resource = ResourceModelMapper.map(
                        resource = resource,
                        name = "Blood",
                    ),
                    actionState = createActionState(),
                    shop = observeUpgrades()
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
        }
    }

    private fun handleUpgradeSelected(action: MainViewAction.UpgradeSelected) {
        viewModelScope.launch {
            buyUpgrade(id = action.id)
        }
    }

    private fun initViewState(): MainViewState = MainViewState.Loading

    private fun createActionState() = ActionsState(
        actionPanes = listOf(
            ActionPane(
                actions = listOf(
                    ActionModel(id = 0L, title = "Work"),
                    ActionModel(id = 1L, title = "Buy a pet"),
                    ActionModel(id = 2L, title = "Eat food"),
                    ActionModel(id = 3L, title = "Pay rent"),
                )
            ),
            ActionPane(
                actions = listOf(
                    ActionModel(id = 100L, title = "Work"),
                    ActionModel(id = 101L, title = "Eat a pet"),
                    ActionModel(id = 102L, title = "Capture man"),
                    ActionModel(id = 103L, title = "Eat man"),
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
