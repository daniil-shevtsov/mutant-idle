package com.daniil.shevtsov.idle.main.domain.resource

import kotlinx.coroutines.flow.Flow

interface ResourceSource {
    operator fun invoke(): Flow<Resource>
}