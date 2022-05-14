package com.daniil.shevtsov.idle.core.di

import com.daniil.shevtsov.idle.core.di.viewmodel.ViewModelModule

import dagger.Module

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
interface AppModule
