package com.daniil.shevtsov.idle.main.domain.upgrade

import kotlinx.coroutines.flow.Flow

interface UpgradeRepository {
    fun observe(): Flow<List<Upgrade>>
    suspend fun getUpgradeBy(id: Long): Upgrade?
    suspend fun updateUpgradeStatus(id: Long, status: UpgradeStatus)
}