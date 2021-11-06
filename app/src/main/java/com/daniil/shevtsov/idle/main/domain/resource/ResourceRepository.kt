package com.daniil.shevtsov.idle.main.domain.resource

import kotlinx.coroutines.flow.Flow

interface ResourceRepository {
    suspend fun getCurrentResource(): Resource
    suspend fun setNewResource(resource: Resource)
    fun observeResource(): Flow<Resource>
}