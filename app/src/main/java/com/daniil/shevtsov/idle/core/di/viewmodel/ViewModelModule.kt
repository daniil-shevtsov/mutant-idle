package com.daniil.shevtsov.idle.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.core.navigation.ScreenHostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ScreenHostViewModel::class)
    fun bindMainViewModel(viewModel: ScreenHostViewModel): ViewModel

    @Binds
    @AppScope
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
