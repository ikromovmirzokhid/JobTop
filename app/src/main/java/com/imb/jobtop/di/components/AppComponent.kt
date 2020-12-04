package com.imb.jobtop.di.components

import android.content.Context
import com.imb.jobtop.App
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import com.imb.jobtop.utils.resources.AndroidResourceProvider
import com.imb.jobtop.utils.resources.ResourceProvider
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun resources(): ResourceProvider

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder

        fun build(): AppComponent
    }

}

@Module
abstract class AppModule(private val application: App) {
    @Binds
    @Singleton
    abstract fun bindResourceProvider(provider: AndroidResourceProvider): ResourceProvider

}