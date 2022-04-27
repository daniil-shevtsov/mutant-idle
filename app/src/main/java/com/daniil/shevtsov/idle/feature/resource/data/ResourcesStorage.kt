package com.daniil.shevtsov.idle.feature.resource.data

import com.daniil.shevtsov.idle.core.data.MultipleStorage
import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@AppScope
class ResourcesStorage @Inject constructor(
    initialResources: List<Resource>
) {

    private val multipleStorage = MultipleStorage(
        initialValues = initialResources,
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

    fun upgradeAll(newResources: List<Resource>) {
        multipleStorage.updateAll(newValues = newResources)
    }

}
