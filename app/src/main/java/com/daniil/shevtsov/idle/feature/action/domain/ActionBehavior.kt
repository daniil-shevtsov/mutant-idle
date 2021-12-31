package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.action.data.ActionsStorage
import kotlinx.coroutines.flow.Flow

object ActionBehavior {

    fun observeAll(storage: ActionsStorage): Flow<List<Action>> {
        return storage.observeAll()
    }

}