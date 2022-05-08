package com.daniil.shevtsov.idle.application

import android.app.Application
import com.daniil.shevtsov.idle.common.di.initKoin
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.di.DaggerAppComponent
import com.daniil.shevtsov.idle.core.di.koin.appModule
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.core.presentation.formatting.formatEnumName
import com.daniil.shevtsov.idle.feature.action.domain.createAllActions
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.flavor.createFlavors
import com.daniil.shevtsov.idle.feature.gamefinish.domain.createEndings
import com.daniil.shevtsov.idle.feature.initial.domain.createBalanceConfig
import com.daniil.shevtsov.idle.feature.initial.domain.createInitialGameState
import com.daniil.shevtsov.idle.feature.location.domain.LocationSelectionState
import com.daniil.shevtsov.idle.feature.location.domain.createLocations
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.main.presentation.SectionState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.Jobs
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.createInitialTraits
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.createResources
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.unlocks.domain.UnlockState
import com.daniil.shevtsov.idle.feature.upgrade.domain.createUpgrades
import org.koin.core.Koin
import timber.log.Timber
import javax.inject.Inject

class IdleGameApplication : Application() {
    lateinit var koin: Koin
    val appComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(
                appContext = applicationContext,
                balanceConfig = createBalanceConfig(),
                initialResources = createResources(),
                initialGameState = createInitialGameState(),
            )
    }

    @Inject
    lateinit var viewModel: IdleGameViewModel


    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        koin = initKoin {
            modules(appModule)
        }.koin

        appComponent.inject(this)

        viewModel.onStart()
    }

    override fun onTerminate() {
        viewModel.onCleared()
        super.onTerminate()
    }

}
