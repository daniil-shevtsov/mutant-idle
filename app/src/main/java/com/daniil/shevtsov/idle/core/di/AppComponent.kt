package com.daniil.shevtsov.idle.core.di

import android.content.Context
import com.daniil.shevtsov.idle.main.MainFragment
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
        ): AppComponent
    }

    fun inject(mainFragment: MainFragment)
}
