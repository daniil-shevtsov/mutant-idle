package com.daniil.shevtsov.idle.main

sealed class MainViewAction {
    data class ActionClicked(val id: Long) : MainViewAction()
    data class UpgradeSelected(val id: Long) : MainViewAction()
}
