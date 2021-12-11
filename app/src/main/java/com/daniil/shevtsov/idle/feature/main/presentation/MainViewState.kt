package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState

sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(
        val resources: List<ResourceModel>,
        val actionState: ActionsState,
        val shop: ShopState,
    ) : MainViewState()
}