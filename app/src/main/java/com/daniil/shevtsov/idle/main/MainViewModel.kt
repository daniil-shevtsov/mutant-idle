package com.daniil.shevtsov.idle.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.BuyUpgradeUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.ObserveUpgradesUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModelMapper
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
                    upgrades = observeUpgrades()
                        .firstOrNull()
                        .orEmpty()
                        .map { upgrade ->
                            UpgradeModelMapper.map(
                                upgrade = upgrade,
                                status = upgrade.mapStatus(resource.value)
                            )
                        },
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

    private fun Upgrade.mapStatus(resource: Double): UpgradeStatusModel {
        val statusModel = when {
            status == UpgradeStatus.Bought -> UpgradeStatusModel.Bought
            price.value <= resource -> UpgradeStatusModel.Affordable
            else -> UpgradeStatusModel.NotAffordable
        }
        return statusModel
    }

}
