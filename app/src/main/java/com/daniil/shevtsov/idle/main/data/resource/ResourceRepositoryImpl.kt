package com.daniil.shevtsov.idle.main.data.resource

import com.daniil.shevtsov.idle.main.domain.resource.Resource
import com.daniil.shevtsov.idle.main.domain.resource.ResourceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ResourceRepositoryImpl @Inject constructor(
    private val storage: ResourceStorage,
) : ResourceRepository {

    override fun observeResource(): Flow<Resource> {
        return storage.observeChange().map(ResourceMapper::map)
    }

    override suspend fun getCurrentResource(): Resource {
        return storage.getCurrentValue()
            .let(ResourceMapper::map)
    }

    override suspend fun setNewResource(resource: Resource) {
        storage.setNewValue(resource.let(ResourceDtoMapper::map))
    }
}