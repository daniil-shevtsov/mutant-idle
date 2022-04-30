package com.daniil.shevtsov.idle.core.di

import android.content.Context
import com.daniil.shevtsov.idle.application.IdleGameApplication
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.navigation.ScreenHostFragment
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
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
            @BindsInstance initialGameState: GameState,
        ): AppComponent
    }

    fun inject(screenHostFragment: ScreenHostFragment)
    fun inject(application: IdleGameApplication)
}
