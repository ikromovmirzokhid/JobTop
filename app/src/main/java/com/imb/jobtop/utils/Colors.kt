package com.imb.jobtop.utils

import androidx.core.graphics.toColorInt
import java.util.*

object Colors {
    fun getRandomColor(): Int {
        val list = listOf(
            "#9AD3BC",
            "#89BEB3",
            "#FFB6B9",
            "#94B4A4",
            "#FFAAA5"
        )
        return list[Random().nextInt(4)].toColorInt()
    }
}