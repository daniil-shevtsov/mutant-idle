package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.UpgradesViewState

sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(
        val resources: List<ResourceModel>,
        val ratios: List<RatioModel> = emptyList(),
        val actionState: ActionsState,
        val locationSelectionViewState: LocationSelectionViewState,
        val shop: UpgradesViewState,
        val sectionCollapse: Map<SectionKey, Boolean> = mapOf(),
        val drawerState: DrawerViewState,
    ) : MainViewState()
}
