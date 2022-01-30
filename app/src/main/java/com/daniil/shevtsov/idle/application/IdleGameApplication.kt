package com.daniil.shevtsov.idle.application

import android.app.Application
import com.daniil.shevtsov.idle.common.di.initKoin
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.di.DaggerAppComponent
import com.daniil.shevtsov.idle.core.di.koin.appModule
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.ActionType
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
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
                initialUpgrades = createInitialUpgrades(),
                initialActions = createInitialActions(),
                initialResources = createInitialResources(),
                initialRatios = createInitialRatios(),
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
        tickRateMillis = 100L,
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

    private fun createInitialActions() = listOf(
        Action(
            id = 0L,
            title = "Work",
            subtitle = "The sun is high",
            actionType = ActionType.Human,
            resourceChanges = mapOf(
                ResourceKey.Money to 25.0
            )
        ),
        Action(
            id = 1L,
            title = "Buy a pet",
            subtitle = "It's so cute",
            actionType = ActionType.Human,
            resourceChanges = mapOf(
                ResourceKey.Money to -50.0,
            )
        ),
        Action(
            id = 3L,
            title = "Buy Groceries",
            subtitle = "It's a short walk",
            actionType = ActionType.Human,
            resourceChanges = mapOf(
                ResourceKey.Money to -15.0,
                ResourceKey.Food to 1.0,
            )
        ),
        Action(
            id = 4L,
            title = "Order Groceries",
            subtitle = "I can hide at home",
            actionType = ActionType.Human,
            resourceChanges = mapOf(
                ResourceKey.Money to -20.0,
                ResourceKey.Food to 1.0,
            )
        ),
        Action(
            id = 5L, title = "Grow", subtitle = "Cultivating mass",
            actionType = ActionType.Mutant,
            resourceChanges = mapOf(
                ResourceKey.Blood to 15.0,
            )
        ),
        Action(
            id = 6L, title = "Eat a pet", subtitle = "Its time is up",
            actionType = ActionType.Mutant,
            resourceChanges = mapOf(
                ResourceKey.Blood to 10.0,
            )
        ),
        Action(
            id = 7L,
            title = "Hunt for rats",
            subtitle = "Surely there are some",
            actionType = ActionType.Mutant,
            resourceChanges = mapOf(
                ResourceKey.Blood to 5.0,
            )
        ),
        Action(
            id = 8L,
            title = "Capture a person",
            subtitle = "I think I can do it if I grow enough",
            actionType = ActionType.Mutant,
            resourceChanges = mapOf(
                ResourceKey.Blood to -10.0,
            ),
            ratioChanges = mapOf(
                RatioKey.Suspicion to 0.1f,
            )
        ),
        Action(
            id = 9L,
            title = "Eat captured person",
            subtitle = "Finally some good fucking food",
            actionType = ActionType.Mutant,
            resourceChanges = mapOf(
                ResourceKey.Blood to 25.0,
            ),
            ratioChanges = mapOf(
                RatioKey.Suspicion to 0.05f,
            )
        ),
        Action(
            id = 10L,
            title = "Eat food",
            subtitle = "It's not enough",
            actionType = ActionType.Human,
            resourceChanges = mapOf(
                ResourceKey.Blood to 2.0,
                ResourceKey.Food to -1.0,
            )
        ),
    )

    private fun createInitialResources() = listOf(
        Resource(key = ResourceKey.Blood, name = "Blood", value = 0.0),
        Resource(key = ResourceKey.Money, name = "Money", value = 0.0),
        Resource(key = ResourceKey.Food, name = "Food", value = 0.0),
    )

    private fun createInitialRatios() = listOf(
        Ratio(key = RatioKey.Mutanity, title = "", value = 0.0),
        Ratio(key = RatioKey.Suspicion, title = "", value = 0.0),
    )

}
