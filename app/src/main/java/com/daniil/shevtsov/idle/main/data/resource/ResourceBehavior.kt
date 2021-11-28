package com.daniil.shevtsov.idle.main.data.resource

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.time.Time
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.domain.resource.UpdateResourcesUseCase
import com.daniil.shevtsov.idle.main.ui.resource.GetCurrentResourceUseCase

object ResourceBehavior {

    private lateinit var observeResourceUseCase: ObserveResourceUseCase
    private lateinit var updateResourcesUseCase: UpdateResourcesUseCase
    private lateinit var getCurrentResourceUseCase: GetCurrentResourceUseCase

    fun create(balanceConfig: BalanceConfig) {
        val storage = ResourceStorage()
        val repository = ResourceRepositoryImpl(
            storage = storage
        )

        observeResourceUseCase = ObserveResourceUseCase(
            resourceRepository = repository
        )
        updateResourcesUseCase = UpdateResourcesUseCase(
            balanceConfig = balanceConfig,
            resourceRepository = repository
        )
        getCurrentResourceUseCase = GetCurrentResourceUseCase(
            resourceRepository = repository
        )
    }

    fun observeResource() = observeResourceUseCase()
    suspend fun updateResource(timePassed: Time) = updateResourcesUseCase(timePassed)
    suspend fun getCurrentResource() = getCurrentResourceUseCase()

}