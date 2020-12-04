package com.imb.jobtop.utils.resources

import android.content.Context
import com.imb.jobtop.utils.resources.ResourceProvider
import javax.inject.Inject

class AndroidResourceProvider @Inject constructor(private val context: Context) : ResourceProvider {
    override fun string(id: Int) = context.resources.getString(id)
}