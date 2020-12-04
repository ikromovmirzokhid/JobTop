package com.imb.jobtop

import com.imb.jobtop.di.components.AppComponent
import com.imb.jobtop.network.di.NetworkComponent

object DI {
    lateinit var appComponent: AppComponent
        internal set

    lateinit var networkComponent: NetworkComponent
        internal set

}