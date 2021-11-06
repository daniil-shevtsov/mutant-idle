package com.daniil.shevtsov.idle.main.data

import com.daniil.shevtsov.idle.main.domain.Resource
import com.daniil.shevtsov.idle.main.domain.ResourceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ResourceRepositoryImpl @Inject constructor(
    private val storage: ResourceStorage,
) : ResourceRepository {

    override fun observeResource(): Flow<Resource> {
        return storage.observeChange().map(ResourceMapper::map)
    }
}