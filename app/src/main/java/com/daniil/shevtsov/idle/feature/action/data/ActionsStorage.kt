package com.daniil.shevtsov.idle.feature.action.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.action.domain.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AppScope
class ActionsStorage @Inject constructor(
    initialActions: List<Action>
) {
    private val actions: MutableStateFlow<Map<Long, Action>> =
        MutableStateFlow(initialActions.associateBy { it.id })

    fun observeAll(): Flow<List<Action>> {
        return actions.map { it.values.toList() }
    }

    fun getActionById(id: Long): Action? {
        val actionMap = actions.value
        val action = actionMap[id]
        return action
    }

    fun updateAction(id: Long, newValue: Action) {
        val actionMap = actions.value
        val modifiedMap = actionMap
            .toMutableMap()
            .apply { put(id, newValue) }
            .toMap()
        actions.value = modifiedMap
    }

}