package com.daniil.shevtsov.idle.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.ObserveUpgradesUseCase
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModelMapper
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModelMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val observeResource: ObserveResourceUseCase,
    private val observeUpgrades: ObserveUpgradesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()

    init {
        observeResource()
            .map { resource ->
                ResourceModelMapper.map(
                    resource = resource,
                    name = "Blood",
                )
            }
            .onEach { resource ->
                _state.value = MainViewState.Success(
                    resource = resource,
                    upgrades = observeUpgrades()
                        .firstOrNull()
                        .orEmpty()
                        .map(UpgradeModelMapper::map),
                )
            }
            .launchIn(viewModelScope)
    }

    private fun initViewState(): MainViewState = MainViewState.Loading

}
