package com.daniil.shevtsov.idle.core.di

import com.daniil.shevtsov.idle.core.di.viewmodel.ViewModelModule
import com.daniil.shevtsov.idle.main.data.resource.ResourceRepositoryImpl
import com.daniil.shevtsov.idle.main.data.time.TimeProvider
import com.daniil.shevtsov.idle.main.data.time.TimeProviderImpl
import com.daniil.shevtsov.idle.main.domain.resource.ResourceRepository
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
interface AppModule {

    @Binds
    @AppScope
    fun resourceRepository(impl: ResourceRepositoryImpl): ResourceRepository

    @Binds
    @AppScope
    fun timeProvider(impl: TimeProviderImpl): TimeProvider
}
