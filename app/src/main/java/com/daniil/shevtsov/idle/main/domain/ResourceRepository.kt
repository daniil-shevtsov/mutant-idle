package com.daniil.shevtsov.idle.main.domain

import kotlinx.coroutines.flow.Flow

interface ResourceRepository {
    fun observeResource(): Flow<Resource>
}