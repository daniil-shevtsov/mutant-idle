package com.daniil.shevtsov.idle.core.di

import android.content.Context
import com.daniil.shevtsov.idle.application.IdleGameApplication
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.main.view.MainFragment
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
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
            @BindsInstance initialUpgrades: List<Upgrade>,
            @BindsInstance initialActions: List<Action>,
            @BindsInstance initialResources: List<Resource>,
            @BindsInstance initialRatios: List<Ratio>,
            @BindsInstance initialPlayer: Player,
            @BindsInstance initialJobs: List<PlayerJob>,
            @BindsInstance initialMainState: MainFunctionalCoreState,
        ): AppComponent
    }

    fun inject(mainFragment: MainFragment)
    fun inject(application: IdleGameApplication)
}
