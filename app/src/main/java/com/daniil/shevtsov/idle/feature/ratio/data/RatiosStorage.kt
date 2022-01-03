package com.daniil.shevtsov.idle.feature.ratio.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AppScope
class RatiosStorage @Inject constructor(
    initialResources: List<Ratio>
) {
    private val storedData: MutableStateFlow<Map<RatioKey, Ratio>> =
        MutableStateFlow(initialResources.associateBy { it.key })

    fun observeAll(): Flow<List<Ratio>> {
        return storedData.map { it.values.toList() }
    }

    fun getByKey(key: RatioKey): Ratio? {
        val map = storedData.value
        return map[key]
    }

    fun updateByKey(key: RatioKey, newValue: Ratio) {
        val map = storedData.value
        val modifiedMap = map
            .toMutableMap()
            .apply { put(key, newValue) }
            .toMap()
        storedData.value = modifiedMap
    }

}