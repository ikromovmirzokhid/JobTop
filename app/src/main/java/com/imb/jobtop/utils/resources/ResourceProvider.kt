package com.imb.jobtop.utils.resources

import androidx.annotation.StringRes

interface ResourceProvider {

    fun string(@StringRes id: Int): String
}