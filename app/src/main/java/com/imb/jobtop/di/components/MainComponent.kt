package com.imb.jobtop.di.components

import androidx.lifecycle.ViewModel
import com.imb.jobtop.DI
import com.imb.jobtop.di.ScreenScope
import com.imb.jobtop.di.ViewModelFactory
import com.imb.jobtop.di.ViewModelKey
import com.imb.jobtop.network.api.VacancyApi
import com.imb.jobtop.utils.resources.ResourceProvider
import com.imb.jobtop.viewmodel.JobViewModel
import com.imb.jobtop.viewmodel.VacancyViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap

@Component(modules = [MainScreenModule::class])
@ScreenScope
interface MainComponent {
    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun resources(resourceProvider: ResourceProvider): Builder

        @BindsInstance
        fun vacancyApi(loanApi: VacancyApi): Builder

        fun build(): MainComponent
    }

    companion object {
        fun create() = with(DI.appComponent) {
            DaggerMainComponent.builder()
                .resources(resources())
                .vacancyApi(DI.networkComponent.vacancyApi())
                .build()
        }
    }
}

@Module
abstract class MainScreenModule {

    @Binds
    @IntoMap
    @ViewModelKey(VacancyViewModel::class)
    abstract fun vacancyViewModel(viewModel: VacancyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobViewModel::class)
    abstract fun vacancyViewModel(viewModel: JobViewModel): ViewModel
}