package com.daniil.shevtsov.idle.main

import androidx.lifecycle.ViewModel
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.ResourceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AndroidMainViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(initViewState())
    val state = _state.asStateFlow()


    private fun initViewState(): MainViewState = MainViewState(
        resource = ResourceModel(text = "100")
    )

}
