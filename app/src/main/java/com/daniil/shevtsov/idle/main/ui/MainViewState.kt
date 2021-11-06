package com.daniil.shevtsov.idle.main.ui

sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(
        val resource: ResourceModel,
    ) : MainViewState()
}