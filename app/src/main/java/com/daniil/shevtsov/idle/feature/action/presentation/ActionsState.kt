package com.daniil.shevtsov.idle.feature.action.presentation

data class ActionsState(
    val actionPanes: List<ActionPane>,
    val humanActionPane: ActionPane,
    val mutantActionPane: ActionPane,
)
