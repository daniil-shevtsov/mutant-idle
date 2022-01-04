package com.daniil.shevtsov.idle.feature.ratio.data

import com.daniil.shevtsov.idle.core.data.MultipleStorage
import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AppScope
class RatiosStorage @Inject constructor(
    initialResources: List<Ratio>
) {

    private val multipleStorage = MultipleStorage(
        initialResources = initialResources,
        keyExtractor = Ratio::key,
    )

    fun observeAll(): Flow<List<Ratio>> = multipleStorage.observeAll()

    fun getByKey(key: RatioKey): Ratio? = multipleStorage.getByKey(key = key)

    fun updateByKey(
        key: RatioKey,
        newValue: Ratio
    ) = multipleStorage.updateByKey(
        key = key,
        newValue = newValue,
    )

}