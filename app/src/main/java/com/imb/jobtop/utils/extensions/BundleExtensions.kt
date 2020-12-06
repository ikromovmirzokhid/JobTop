package com.imb.jobtop.utils.extensions

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun <T> Bundle.putData(key: String, data: T) {
    this.putString(key, Gson().toJson(data))
}

fun Bundle.getData(key: String): String? {
    return this.getString(key)
}

inline fun <reified T> Bundle.getData(key: String): T? {
    return Gson().fromJson(getData(key), T::class.java)
}

inline fun <reified T> Bundle.getList(key: String): List<T>? {
    val gson = Gson()
    val itemType = object : TypeToken<List<T>>() {}.type
    return gson.fromJson<List<T>>(getData(key), itemType)
}