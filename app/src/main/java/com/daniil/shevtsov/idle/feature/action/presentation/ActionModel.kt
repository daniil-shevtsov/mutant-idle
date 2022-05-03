package com.daniil.shevtsov.idle.feature.action.presentation

data class ActionModel(
    val id: Long,
    val title: String,
    val subtitle: String,
    val icon: ActionIcon,
    val resourceChanges: List<ResourceChangeModel>,
    val ratioChanges: List<RatioChangeModel>,
    val isEnabled: Boolean = true,
)
