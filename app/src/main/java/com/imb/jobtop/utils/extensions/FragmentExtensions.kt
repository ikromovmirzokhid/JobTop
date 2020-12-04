package com.imb.jobtop.utils.extensions

import android.app.AlertDialog
import com.imb.jobtop.fragments.base.BaseFragment

fun BaseFragment.progressOn() {
    getMain()?.showProgress()
}

fun BaseFragment.progressOff() {
    getMain()?.hideProgress()
}

fun BaseFragment.showError(errorMessage: String?) {
    context?.apply {
        AlertDialog.Builder(this)
            .setTitle("Внимание")
            .setMessage(errorMessage)
            .setPositiveButton("OK", null)
            .show()
    }
}