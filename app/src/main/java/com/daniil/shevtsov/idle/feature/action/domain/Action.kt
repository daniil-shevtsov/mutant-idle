package com.daniil.shevtsov.idle.feature.action.domain

data class Action(
    val id: Long,
    val title: String,
    val subtitle: String,
    val actionType: ActionType,
)