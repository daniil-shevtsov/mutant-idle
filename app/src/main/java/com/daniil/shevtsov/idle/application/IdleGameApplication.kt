package com.daniil.shevtsov.idle.application

import android.app.Application
import com.daniil.shevtsov.idle.common.di.initKoin
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.di.DaggerAppComponent
import com.daniil.shevtsov.idle.core.di.koin.appModule
import com.daniil.shevtsov.idle.feature.action.domain.createAllActions
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.main.presentation.SectionState
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.job.domain.Jobs
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.createResources
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.upgrade.domain.Price
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
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
                initialMainState = MainFunctionalCoreState(
                    balanceConfig = createBalanceConfig(),
                    resources = createResources(),
                    ratios = createInitialRatios(),
                    sections = createInitialSectionState(),
                    drawerTabs = createInitialDrawerTabs(),
                    upgrades = createInitialUpgrades(),
                    actions = createAllActions(),
                    availableJobs = createInitialJobs(),
                    player = createInitialPlayer(),
                )
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

    private fun createBalanceConfig() = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 0.002,
        resourceSpentForFullMutant = 100.0,
    )

    private fun createInitialUpgrades() = listOf(
        Upgrade(
            id = 0L,
            title = "Hand-sword",
            subtitle = "Transform your hand into a sharp blade",
            price = Price(value = 50.0),
            status = UpgradeStatus.NotBought,
        ),
        Upgrade(
            id = 1L,
            title = "Fangs",
            subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though",
            price = Price(value = 25.0),
            status = UpgradeStatus.NotBought,
        ),
        Upgrade(
            id = 2L,
            title = "Iron jaws",
            subtitle = "Your jaws become stronger than any shark",
            price = Price(value = 10.0),
            status = UpgradeStatus.NotBought,
        ),
    )

    private fun createInitialRatios() = listOf(
        Ratio(key = RatioKey.Mutanity, title = "", value = 0.0),
        Ratio(key = RatioKey.Suspicion, title = "", value = 0.0),
    )

    private fun createInitialSectionState() = listOf(
        SectionState(key = SectionKey.Resources, isCollapsed = false),
        SectionState(key = SectionKey.Actions, isCollapsed = false),
        SectionState(key = SectionKey.Upgrades, isCollapsed = false),
    )

    private fun createInitialDrawerTabs() = listOf(
        DrawerTab(id = DrawerTabId.PlayerInfo, title = "Player Info", isSelected = false),
        DrawerTab(id = DrawerTabId.Debug, title = "Debug", isSelected = false),
    )

    private fun createInitialPlayer() = Player(
        job = Jobs.Mortician,
        tags = Jobs.Mortician.tags + listOf(
            Tags.Devourer,
            Tags.PersonCapturer,
        )
    )

    private fun createInitialJobs() = listOf(
        Jobs.Mortician,
        Jobs.Undertaker,
        Jobs.Butcher,
    )

}
