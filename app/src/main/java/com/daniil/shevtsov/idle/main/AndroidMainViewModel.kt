package com.daniil.shevtsov.idle.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.ResourceModelMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AndroidMainViewModel @Inject constructor(
    private val observeResource: ObserveResourceUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()

    init {
        observeResource()
            .map(ResourceModelMapper::map)
            .onEach { resource ->
                _state.value = MainViewState.Success(
                    resource = resource
                )
            }
            .launchIn(viewModelScope)
    }

    private fun initViewState(): MainViewState = MainViewState.Loading

}
