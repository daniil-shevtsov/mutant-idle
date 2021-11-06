package com.daniil.shevtsov.idle.main.domain.time

import com.daniil.shevtsov.idle.main.data.time.TimeProvider
import javax.inject.Inject
import kotlin.time.Duration

class StartTimeUseCase @Inject constructor(
    private val timeProvider: TimeProvider,
) {

    suspend operator fun invoke() = timeProvider.startUpdatingItemWith(
        interval = Duration.milliseconds(100)
    )

}