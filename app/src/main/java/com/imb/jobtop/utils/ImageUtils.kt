package com.imb.jobtop.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log

fun Bitmap.horizontalRotate(): Bitmap {
    val h = this.height
    val w = this.width
    if (h > w) {
        Log.d("LLLL", "bitmap vertical")
        return this.rotate(90f)
    } else {
        Log.d("LLLL", "bitmap horizontal")
        return this.rotate(0f)
    }
}

fun Bitmap.verticalRotate(): Bitmap {
    val h = this.height
    val w = this.width
    if (h < w) {
        Log.d("LLLL", "bitmap vertical")
        return this.rotate(90f)
    } else {
        Log.d("LLLL", "bitmap horizontal")
        return this.rotate(0f)
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}