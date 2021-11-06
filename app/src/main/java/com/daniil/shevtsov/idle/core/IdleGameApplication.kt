package com.daniil.shevtsov.idle.core

import android.app.Application
import com.daniil.shevtsov.idle.common.di.initKoin
import com.daniil.shevtsov.idle.core.di.DaggerAppComponent
import com.daniil.shevtsov.idle.core.di.koin.appModule
import org.koin.core.Koin

class IdleGameApplication : Application() {
    lateinit var koin: Koin
    val appComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(
                appContext = applicationContext,
            )
    }

    override fun onCreate() {
        super.onCreate()

        koin = initKoin {
            modules(appModule)
        }.koin
    }
}
