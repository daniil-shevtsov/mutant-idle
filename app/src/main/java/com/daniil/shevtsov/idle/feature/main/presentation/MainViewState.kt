package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.EndingViewState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.UnlockModel
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState

sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(
        val resources: List<ResourceModel>,
        val ratios: List<HumanityRatioModel> = emptyList(),
        val actionState: ActionsState,
        val locationSelectionViewState: LocationSelectionViewState,
        val shop: ShopState,
        val sectionCollapse: Map<SectionKey, Boolean> = mapOf(),
        val drawerState: DrawerViewState,
    ) : MainViewState()

    data class GameFinishedState(
        val ending: EndingViewState,
        val unlocks: List<UnlockModel>,
    )
}
