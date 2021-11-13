package com.daniil.shevtsov.idle.main.ui

import com.daniil.shevtsov.idle.main.ui.actions.ActionsState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
import com.daniil.shevtsov.idle.main.ui.shop.ShopState

sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(
        val resource: ResourceModel,
        val actionState: ActionsState,
        val shop: ShopState,
    ) : MainViewState()
}