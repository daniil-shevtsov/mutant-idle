package com.daniil.shevtsov.idle.core.di

import android.content.Context
import com.daniil.shevtsov.idle.application.IdleGameApplication
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.main.view.MainFragment
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance appContext: Context,
            @BindsInstance balanceConfig: BalanceConfig,
            @BindsInstance initialResources: List<Resource>,
            @BindsInstance initialMainState: MainFunctionalCoreState,
        ): AppComponent
    }

    fun inject(mainFragment: MainFragment)
    fun inject(application: IdleGameApplication)
}
