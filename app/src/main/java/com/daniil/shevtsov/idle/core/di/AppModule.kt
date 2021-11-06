package com.daniil.shevtsov.idle.core.di

import com.daniil.shevtsov.idle.core.di.viewmodel.ViewModelModule
import com.daniil.shevtsov.idle.main.data.ResourceRepositoryImpl
import com.daniil.shevtsov.idle.main.domain.ResourceRepository
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
interface AppModule {
    @Binds
    fun resourceRepository(impl: ResourceRepositoryImpl): ResourceRepository
}
