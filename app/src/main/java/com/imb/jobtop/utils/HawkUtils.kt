package com.imb.jobtop.utils


import com.google.gson.Gson
import com.imb.jobtop.model.User
import com.orhanobut.hawk.Hawk

object HawkUtils {

    var firstOpened: Boolean
        get() = Hawk.get("FIRST_OPEN", false)
        set(value) {
            Hawk.put("FIRST_OPEN", value)
        }

    var userLoggedIn: Boolean
        get() = Hawk.get("LOGGED_IN", false)
        set(value) {
            Hawk.put("LOGGED_IN", value)
        }

    var user: User
        get() {
            return Gson().fromJson(
                Hawk.get(
                    "user_data",
                    Gson().toJson(User())
                ),
                User::class.java
            )
        }
        set(value) {
            Hawk.put("user_data", Gson().toJson(value))
        }
}