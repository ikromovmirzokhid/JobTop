package com.imb.jobtop

import android.app.Application
import com.imb.jobtop.di.components.DaggerAppComponent
import com.imb.jobtop.network.di.DaggerNetworkComponent
import com.orhanobut.hawk.Hawk

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        initDI()
    }

    private fun initDI() {
        DI.appComponent = DaggerAppComponent.builder()
            .appContext(this)
            .build()
        DI.networkComponent = DaggerNetworkComponent.builder()
            .appContext(this)
            .build()
    }
}