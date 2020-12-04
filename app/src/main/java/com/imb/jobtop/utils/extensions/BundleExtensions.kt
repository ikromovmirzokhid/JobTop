package com.imb.jobtop.utils.extensions

import android.os.Bundle
import com.google.gson.Gson


fun <T> Bundle.putData(key: String, data: T) {
    this.putString(key, Gson().toJson(data))
}

fun Bundle.getData(key: String): String? {
    return this.getString(key)
}

inline fun<reified T> Bundle.getData(key: String): T? {
    return Gson().fromJson(getData(key), T::class.java)
}