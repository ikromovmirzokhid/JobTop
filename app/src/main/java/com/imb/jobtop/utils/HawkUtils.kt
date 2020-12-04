package com.imb.jobtop.utils


import com.orhanobut.hawk.Hawk

object HawkUtils {

    var firstOpened: Boolean
        get() = Hawk.get("FIRST_OPEN", false)
        set(value) {
            Hawk.put("FIRST_OPEN", value)
        }
}