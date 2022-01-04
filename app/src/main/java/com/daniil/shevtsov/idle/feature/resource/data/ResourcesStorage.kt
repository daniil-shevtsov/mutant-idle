package com.daniil.shevtsov.idle.feature.resource.data

import com.daniil.shevtsov.idle.core.data.MultipleStorage
import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AppScope
class ResourcesStorage @Inject constructor(
    initialResources: List<Resource>
) {

    private val multipleStorage = MultipleStorage(
        initialResources = initialResources,
        keyExtractor = Resource::key,
    )

    fun observeAll(): Flow<List<Resource>> = multipleStorage.observeAll()

    fun getByKey(key: ResourceKey): Resource? = multipleStorage.getByKey(key = key)

    fun updateByKey(
        key: ResourceKey,
        newValue: Resource
    ) = multipleStorage.updateByKey(
        key = key,
        newValue = newValue,
    )

}