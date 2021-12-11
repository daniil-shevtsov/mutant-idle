package com.daniil.shevtsov.idle.core.di

import com.daniil.shevtsov.idle.core.di.viewmodel.ViewModelModule
import com.daniil.shevtsov.idle.feature.time.data.TimeProvider
import com.daniil.shevtsov.idle.feature.time.data.TimeProviderImpl
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
    fun timeProvider(impl: TimeProviderImpl): TimeProvider
}
