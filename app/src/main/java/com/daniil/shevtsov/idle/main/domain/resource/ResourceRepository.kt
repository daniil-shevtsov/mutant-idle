package com.daniil.shevtsov.idle.main.domain.resource

import kotlinx.coroutines.flow.Flow

interface ResourceRepository {
    suspend fun getCurrentResource(): Resource
    suspend fun increaseBy(value: Double)
    suspend fun decreaseBy(value: Double)
    fun observeResource(): Flow<Resource>
}