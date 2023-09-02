package com.daniil.shevtsov.idle.core.di

import com.daniil.shevtsov.idle.core.di.viewmodel.ViewModelModule
import com.daniil.shevtsov.idle.feature.menu.domain.GameTitleRepository
import com.daniil.shevtsov.idle.feature.menu.domain.GameTitleRepositoryImpl
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
    fun gameTitleRepository(impl: GameTitleRepositoryImpl): GameTitleRepository
}
