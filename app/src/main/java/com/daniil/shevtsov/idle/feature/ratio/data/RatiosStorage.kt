package com.daniil.shevtsov.idle.feature.ratio.data

import com.daniil.shevtsov.idle.core.data.MultipleStorage
import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@AppScope
class RatiosStorage @Inject constructor(
    initialRatios: List<Ratio>
) {

    private val multipleStorage = MultipleStorage(
        initialValues = initialRatios,
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

    fun updateByKey(
        key: RatioKey,
        newRatio: Double,
    ) = multipleStorage.updateByKey(
        key = key,
        transformation = { oldRatio -> oldRatio!!.copy(value = newRatio) }
    )

}