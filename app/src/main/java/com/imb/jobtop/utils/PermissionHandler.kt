package com.imb.jobtop.utils

import android.content.Context
import android.util.Log
import com.nabinbhandari.android.permissions.PermissionHandler
import java.util.ArrayList

class PermissionHandler : PermissionHandler() {
    private var l2: (() -> Unit)? = null
    private var l1: ((context: Context?, deniedPermissions: ArrayList<String>?) -> Unit)? = null

    override fun onGranted() {
        l2!!.invoke()
    }

    fun onGranted(f: () -> Unit) {
        Log.d("SMS", "onGranted")
        l2 = f
    }


    override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
        Log.d("SMS", "onDenied")
        l1?.invoke(context, deniedPermissions)
    }

    fun onDenied(f: (context: Context?, deniedPermissions: ArrayList<String>?) -> Unit) {
        Log.d("SMS", "onDenied")
        l1 = f
    }
}